package cn.piesat.framework.log.core;

import cn.piesat.framework.log.utils.ThreadMdcUtil;
import org.slf4j.MDC;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * <p/>
 * {@code @description}: Mdc线程执行器
 * <p/>
 * {@code @create}: 2024-12-23 16:31
 * {@code @author}: zhouxp
 */
public class MdcThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {
    /**
     * 执行任务，传递父线程的MDC上下文信息到子线程中
     */
    @Override
    public void execute(Runnable task) {
        super.execute(ThreadMdcUtil.wrap(task, MDC.getCopyOfContextMap()));
    }

    /**
     * 提交带有返回值的任务，传递父线程的MDC上下文信息到子线程中
     */
    @Override
    public <T> Future<T> submit( Callable<T> task) {
        return super.submit(ThreadMdcUtil.wrap(task, MDC.getCopyOfContextMap()));
    }

    /**
     * 提交无返回值的任务，传递父线程的MDC上下文信息到子线程中
     */
    @Override
    public Future<?> submit( Runnable task) {
        return super.submit(ThreadMdcUtil.wrap(task, MDC.getCopyOfContextMap()));
    }
}
