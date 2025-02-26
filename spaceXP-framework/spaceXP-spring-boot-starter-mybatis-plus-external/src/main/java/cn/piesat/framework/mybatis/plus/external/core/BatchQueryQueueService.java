package cn.piesat.framework.mybatis.plus.external.core;


import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * <p/>
 * {@code @description}: 合并批处理查询
 * <p/>
 * {@code @create}: 2025-02-24 19:44:35
 * {@code @author}: zhouxp
 */
@Slf4j
public  abstract class  BatchQueryQueueService <T,R> {

    private static final int MAX_TASK_NUM = 100;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private final LinkedBlockingQueue<WrapRequest<T, R>> QUEUE = new LinkedBlockingQueue<>();
    protected void init(Function<List<WrapRequest<T, R>>, Map<String, R>> fun) {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                List<WrapRequest<T, R>> list = new ArrayList<>();
                QUEUE.drainTo(list, MAX_TASK_NUM);
                if (list.isEmpty()) {
                    return;
                }
                log.info("合并了 {} 个请求", list.size());
                // 拿到我们需要去数据库查询的特征,保存为集合
                Map<String, R> response = fun.apply(list);
                for (WrapRequest<T, R> req : list) {
                    // 这里再把结果放到队列里
                    R r = response.get(req.getId());
                    if (r != null) {
                        req.resultQueue.offer(r);
                    } else {
                        log.info("未找到请求 {} 的结果", req.getId());
                    }
                }
            } catch (Exception e) {
                log.error("定时任务执行失败: " + e.getMessage(), e);
            }
        }, 100, 10, TimeUnit.MILLISECONDS);
    }


    protected R queryResult(T id) {
        WrapRequest<T, R> request = new WrapRequest<>();
        // 这里用UUID做请求id
        request.id = UUID.randomUUID().toString().replace("-", "");
        request.dataId = id;
        LinkedBlockingQueue<R> resultQueue = new LinkedBlockingQueue<>();
        request.resultQueue = resultQueue;
        // 将对象传入队列
        QUEUE.offer(request);
        try {
            // 取出元素时，如果队列为空，给定阻塞多少毫秒再队列取值，这里是3秒
            return resultQueue.poll(3000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 恢复中断状态
            log.error("查询超时 " ,e);
        }
        return null;
    }

}
