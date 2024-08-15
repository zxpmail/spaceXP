package cn.piesat.tests.redis.controller;

import cn.piesat.framework.redis.external.core.RedissonLock;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p/>
 * {@code @description}:
 * <p/>
 * {@code @create}: 2024-08-15 9:50
 * {@code @author}: zhouxp
 */
@Service
public class TestService {
    @Resource
    private RedissonLock redissonLock;

    public void testLock() {
        subLock();
    }

    public void subLock() {
        try {
            redissonLock.setLock("cn:piesat:lock");
            redissonLock.tryLock();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            redissonLock.unlock();
        }
    }
}
