package cn.piesat.framework.common.lock;

import java.util.concurrent.TimeUnit;

/**
 * <p/>
 * {@code @description}: 分布式接口
 * <p/>
 * {@code @create}: 2024-08-14 18:01
 * {@code @author}: zhouxp
 */
public interface DistributedLock {
    boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException;

    boolean tryLock(long waitTime,  TimeUnit unit) throws InterruptedException;

    boolean tryLock() throws InterruptedException;

    void lock(long leaseTime, TimeUnit unit);

    void unlock();

    boolean isLocked();

    boolean isHeldByThread(long threadId);

    boolean isHeldByCurrentThread();
}
