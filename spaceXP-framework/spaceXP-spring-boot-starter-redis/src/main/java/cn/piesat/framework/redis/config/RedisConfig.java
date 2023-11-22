package cn.piesat.framework.redis.config;


import cn.piesat.framework.redis.core.RedisService;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * <p>对象以json形式保存到redis中</p>
 * @author :zhouxp
 * {@code @date} 2022/9/28 14:02
 * {@code @description} :
 */
@EnableCaching
@AutoConfigureBefore(RedisAutoConfiguration.class)
@Configuration(proxyBeanMethods = false)
public class RedisConfig {
    @Bean
    public RedisService redisService(RedisTemplate<String,Object> redisTemplate){
        return  new RedisService(redisTemplate);
    }
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory){
        //为了方便，一般直接使用<String,Object>
        RedisTemplate<String, Object>  redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        // String的序列化
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // json序列化配置
        GenericFastJsonRedisSerializer jackson2JsonRedisSerializer = new GenericFastJsonRedisSerializer();
        //key采用String的序列化方式
        redisTemplate.setKeySerializer(stringRedisSerializer);
        //hash的key也采用String 的序列化方式
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        //value的序列化方式采用jackson的方式
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        //hash的value序列化方式采用jackson
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
