package cn.piesat.tests.redis.service;

import cn.piesat.framework.common.model.vo.ApiResult;
import cn.piesat.framework.redis.bean.RedisQueueMessage;
import cn.piesat.framework.redis.core.RedisQueueHandleService;
import cn.piesat.tests.redis.entity.UserDO;
import cn.piesat.tests.redis.mapper.UserMapper;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p/>
 * {@code @description}:
 * <p/>
 * {@code @create}: 2025-02-27 10:56:28
 * {@code @author}: zhouxp
 */
@Service
public class SampleRedisQueueHandleService implements RedisQueueHandleService {
    @Setter(onMethod_ = @Autowired)
    private UserMapper userMapper;
    @Override
    public ApiResult<Object> doHandler(RedisQueueMessage redisQueueMessage) {
        System.out.println("消费到数据 " + redisQueueMessage);
        UserDO entity = new UserDO();
        entity.setUsername(redisQueueMessage.getId());
        userMapper.insert(entity);
        if (true){
            throw new RuntimeException("测试事务回滚");
        }
        return ApiResult.ok();
    }
}
