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
@SuppressWarnings({"rawtypes","unchecked"})
@Slf4j
public abstract class BatchQueryQueueService <T,R> {
    int MAX_TASK_NUM = 100;
    Queue<WrapRequest > queue = new LinkedBlockingQueue<>();
    protected void init(Function<List<WrapRequest>, Map<String, R>> fun) {
        //定时任务线程池,创建一个支持定时、周期性或延时任务的限定线程数目(这里传入的是1)的线程池
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(()->{
            int size = queue.size();
            //如果队列没数据,表示这段时间没有请求,直接返回
            if (size == 0) {
                return;
            }
            List<WrapRequest> list = new ArrayList<>();
            log.info("合并了 {} 个请求",size);
            //将队列的请求消费到一个集合保存
            for (int i = 0; i < size; i++) {
                // 后面的SQL语句是有长度限制的，所以还要做限制每次批量的数量,超过最大任务数，等下次执行
                if (i < MAX_TASK_NUM) {
                    list.add(queue.poll());
                }
            }
            //拿到我们需要去数据库查询的特征,保存为集合
            List<WrapRequest> reqs = new ArrayList<>(list);
            Map<String, R> response =fun(reqs);
            for (WrapRequest req : reqs) {
                // 这里再把结果放到队列里
                R r = response.get(req.getId());
                req.resultQueue.offer(r);
            }
        }, 100, 10, TimeUnit.MILLISECONDS);
    }

    protected abstract Map<String,R> fun(List<WrapRequest> reqs);

    protected R queryR(T id) {
        WrapRequest request = new WrapRequest();
        // 这里用UUID做请求id
        request.id = UUID.randomUUID().toString().replace("-", "");
        request.dataId = id;
        LinkedBlockingQueue<R> resultQueue = new LinkedBlockingQueue<>();
        request.resultQueue = resultQueue;
        //将对象传入队列
        queue.offer(request);
        //取出元素时，如果队列为空，给定阻塞多少毫秒再队列取值，这里是3秒
        try {
            return resultQueue.poll(3000,TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.error("查询超时",e);
        }
        return null;
    }

}
