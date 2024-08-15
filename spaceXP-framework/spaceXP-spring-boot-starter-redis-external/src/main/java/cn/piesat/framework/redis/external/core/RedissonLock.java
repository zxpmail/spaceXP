package cn.piesat.framework.redis.external.core;

import cn.piesat.framework.common.lock.DistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * <p/>
 * {@code @description}: redisson锁工具
 * <p/>
 * {@code @create}: 2024-08-15 8:41
 * {@code @author}: zhouxp
 */
@Slf4j
public class RedissonLock implements DistributedLock {
    private final RedissonClient redissonClient;

    private RLock rLock;

    public void setLock(String key) {
        this.rLock = redissonClient.getLock(key);
    }

    public RedissonLock(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    private boolean doTryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException {
        if (rLock == null) {
            throw new RuntimeException("lock is null");
        }
        if (unit == null) {
            return rLock.tryLock();
        } else if (leaseTime == -1) {
            return rLock.tryLock(waitTime, unit);
        } else {
            return rLock.tryLock(waitTime, leaseTime, unit);
        }
    }

    @Override
    public boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException {
        boolean isLockSuccess = doTryLock(waitTime, leaseTime, unit);
        if (log.isDebugEnabled()) {
            log.debug("get lock result:{}", isLockSuccess);
        }
        return isLockSuccess;
    }

    @Override
    public boolean tryLock(long waitTime, TimeUnit unit) throws InterruptedException {
        return doTryLock(waitTime, -1, unit);
    }

    @Override
    public boolean tryLock() throws InterruptedException {
        return doTryLock(0, -1, null);
    }

    @Override
    public void lock(long leaseTime, TimeUnit unit) {
        if (rLock == null) {
            throw new RuntimeException("lock is null");
        }
        rLock.lock(leaseTime, unit);
    }

    @Override
    public void unlock() {
        if (rLock == null) {
            throw new RuntimeException("lock is null");
        }
        rLock.unlock();
    }

    @Override
    public boolean isLocked() {
        if (rLock == null) {
            throw new RuntimeException("lock is null");
        }
        return rLock.isLocked();
    }

    @Override
    public boolean isHeldByThread(long threadId) {
        if (rLock == null) {
            throw new RuntimeException("lock is null");
        }
        return rLock.isHeldByThread(threadId);
    }

    @Override
    public boolean isHeldByCurrentThread() {
        if (rLock == null) {
            throw new RuntimeException("lock is null");
        }
        return rLock.isHeldByCurrentThread();
    }
}
