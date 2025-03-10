package cn.piesat.framework.redis.config;


import cn.piesat.framework.redis.core.*;
import cn.piesat.framework.redis.properties.RedisProperties;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * <p>对象以json形式保存到redis中</p>
 * @author :zhouxp
 * {@code @date} 2022/9/28 14:02
 * {@code @description} :
 */
@EnableCaching
@AutoConfigureBefore(RedisAutoConfiguration.class)
@EnableConfigurationProperties({RedisProperties.class})
@Configuration(proxyBeanMethods = false)
public class RedisConfig {
    @Value("${spring.application.name:test}")
    private String module;
    @Bean
    public RedisService redisService(RedisTemplate<String,Object> redisTemplate){
        return  new RedisService(redisTemplate);
    }
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory,RedisProperties redisProperties){
        //为了方便，一般直接使用<String,Object>
        RedisTemplate<String, Object>  redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        // String的序列化
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        //key采用String的序列化方式
        redisTemplate.setKeySerializer(stringRedisSerializer);
        //hash的key也采用String 的序列化方式
        if(redisProperties.getHashKeyIsString()) {
            redisTemplate.setHashKeySerializer(stringRedisSerializer);
        }
        GenericFastJsonRedisSerializer jackson2JsonRedisSerializer = new GenericFastJsonRedisSerializer();
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        if (redisProperties.getCompressEnable()){
            CompressRedisSerializer<?> jsonSerializer = new CompressRedisSerializer<>(Object.class);
            //hash的value序列化方式采用压缩
            redisTemplate.setHashValueSerializer(jsonSerializer);
        }else{
            // json序列化配置

            //value的序列化方式采用jackson的方式
            redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
            //hash的value序列化方式采用jackson
            redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        }

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
    @Bean
    @ConditionalOnProperty(name = "space.redis.message-enable", havingValue = "true")
    public RedisMessageListener redisMessageListener(){
        return new RedisMessageListener();
    }
    @Bean
    @ConditionalOnProperty(name = "space.redis.message-enable", havingValue = "true")
    public RedisMessageListenerContainer container(RedisProperties redisProperties,RedisConnectionFactory redisConnectionFactory,
                                                   RedisMessageListener listener) {


        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        // 监听所有库的key过期事件
        container.setConnectionFactory(redisConnectionFactory);
        // 所有的订阅消息，都需要在这里进行注册绑定,new PatternTopic(TOPIC_NAME)表示发布的主题信息
        // 可以添加多个 messageListener，配置不同的通道
        //todo  目前支持一个,以后扩展
        container.addMessageListener(listener, new PatternTopic(redisProperties.getTopics()));

        /*
         * 设置序列化对象
         * 特别注意：1. 发布的时候需要设置序列化；订阅方也需要设置序列化
         *         2. 设置序列化对象必须放在[加入消息监听器]这一步后面，否则会导致接收器接收不到消息
         */
        Jackson2JsonRedisSerializer<String> seria = new Jackson2JsonRedisSerializer<>(String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),ObjectMapper.DefaultTyping.NON_FINAL);
        seria.setObjectMapper(objectMapper);
        container.setTopicSerializer(seria);

        return container;
    }

    @SuppressWarnings("deprecation")
    @Bean
    @ConditionalOnProperty(name = "space.redis.prevent-replay-enable", havingValue = "true")
    public PreventReplayAspect preventAspect(RedisService redisService){
        return new PreventReplayAspect(redisService, module);
    }

    @Bean
    @ConditionalOnProperty(name = "space.redis.access-limit-enable", havingValue = "true")
    public AccessLimitInterceptor accessLimitInterceptor(RedisProperties redisProperties){
        return new AccessLimitInterceptor(redisProperties);
    }

    @Bean
    @ConditionalOnBean(AccessLimitInterceptor.class)
    public RedisWebConfiguration idempotentWebConfiguration(AccessLimitInterceptor accessLimitInterceptor){
        return new RedisWebConfiguration(accessLimitInterceptor);
    }

    @Bean
    @ConditionalOnProperty(name = "space.redis.delaying-queue-enable", havingValue = "true")
    public DelayingQueueService delayingQueueService(){
        return new DelayingQueueService();
    }
}
