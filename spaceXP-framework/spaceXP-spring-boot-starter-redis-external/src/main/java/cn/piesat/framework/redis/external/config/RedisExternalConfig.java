package cn.piesat.framework.redis.external.config;



import cn.piesat.framework.redis.external.core.DistributedLockAspect;
import cn.piesat.framework.redis.external.core.RedissonLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>对象以json形式保存到redis中</p>
 * @author :zhouxp
 * {@code @date} 2022/9/28 14:02
 * {@code @description} :
 */
@Configuration(proxyBeanMethods = false)
public class RedisExternalConfig {
    @Bean
    public DistributedLockAspect DistributedLock(RedissonClient redissonClient){
        return new DistributedLockAspect(redissonClient);
    }

    @Bean
    public RedissonLock getRedissonLock(RedissonClient redissonClient){
        return new RedissonLock(redissonClient);
    }
}
