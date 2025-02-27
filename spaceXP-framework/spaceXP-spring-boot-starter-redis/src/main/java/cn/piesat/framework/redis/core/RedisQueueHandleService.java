package cn.piesat.framework.redis.core;

import cn.piesat.framework.redis.bean.RedisQueueMessage;

/**
 * <p/>
 * {@code @description}: redis队列消息处理接口
 * <p/>
 * {@code @create}: 2025-02-27 09:53:39
 * {@code @author}: zhouxp
 */
public interface RedisQueueHandleService {
    void  handler(RedisQueueMessage redisQueueMessage);
}
