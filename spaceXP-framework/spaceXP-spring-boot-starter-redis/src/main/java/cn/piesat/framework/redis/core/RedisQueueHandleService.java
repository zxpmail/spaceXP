package cn.piesat.framework.redis.core;

import cn.piesat.framework.common.model.vo.ApiResult;
import cn.piesat.framework.redis.bean.RedisQueueMessage;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p/>
 * {@code @description}: redis队列消息处理接口
 * <p/>
 * {@code @create}: 2025-02-27 09:53:39
 * {@code @author}: zhouxp
 */
public interface RedisQueueHandleService {

    ApiResult<Object>  doHandler(RedisQueueMessage redisQueueMessage);

    @Transactional(rollbackFor = Exception.class)
    default void handler(RedisQueueMessage redisQueueMessage) {
        before(redisQueueMessage);
        ApiResult<Object> result = doHandler(redisQueueMessage);
        after(redisQueueMessage,result);
    }

    default void after(RedisQueueMessage redisQueueMessage, ApiResult<Object> result){

    }

    default void before(RedisQueueMessage redisQueueMessage){

    }
}
