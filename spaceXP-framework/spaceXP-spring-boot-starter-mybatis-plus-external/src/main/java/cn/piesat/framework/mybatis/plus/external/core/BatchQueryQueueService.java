package cn.piesat.framework.mybatis.plus.external.core;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

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
public abstract class BatchQueryQueueService<T, R> {
    @Value("${space.db.external.max-task-num:100}")
    private Integer maxTaskNum;
    @Value("${space.db.external.core_pool_size:1}")
    private Integer corePoolSize;
    @Value("${space.db.external.time_out:3000}")
    private Integer timeOut;
    @Value("${space.db.external.init_delay:100}")
    private Integer initDelay  ;
    @Value("${space.db.external.period:10}")
    private Integer period ;
    private final LinkedBlockingQueue<WrapRequest<T, R>> QUEUE = new LinkedBlockingQueue<>();

    protected void init(Function<List<WrapRequest<T, R>>, Map<String, R>> fun) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(corePoolSize);
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                List<WrapRequest<T, R>> list = new ArrayList<>();
                QUEUE.drainTo(list, maxTaskNum);
                if (list.isEmpty()) {
                    return;
                }
                log.info("合并了 {} 个请求", list.size());
                // 拿到我们需要去数据库查询的特征,保存为集合
                Map<String, R> response = fun.apply(list);
                for (WrapRequest<T, R> req : list) {
                    // 这里再把结果放到队列里
                    R r = response.get(req.requestId);
                    if (r != null) {
                        req.response.offer(r);
                    } else {
                        log.info("未找到请求 {} 的结果", req.getRequestId());
                    }
                }
            } catch (Exception e) {
                log.error("定时任务执行失败: " + e.getMessage(), e);
            }
        }, initDelay, period, TimeUnit.MILLISECONDS);
    }


    protected R queryResult(T params) {
        WrapRequest<T, R> request = new WrapRequest<>();
        // 这里用UUID做请求id
        request.requestId = UUID.randomUUID().toString().replace("-", "");
        request.params = params;
        LinkedBlockingQueue<R> resultQueue = new LinkedBlockingQueue<>();
        request.response = resultQueue;
        // 将对象传入队列
        QUEUE.offer(request);
        try {
            // 取出元素时，如果队列为空，给定阻塞多少毫秒再队列取值，这里是3秒
            return resultQueue.poll(timeOut, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 恢复中断状态
            log.error("查询超时 ", e);
        }
        return null;
    }

}
