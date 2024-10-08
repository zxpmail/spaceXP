package cn.piesat.framework.redis.core;

import cn.piesat.framework.common.model.entity.MessageEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;

import javax.annotation.Resource;

/**
 * <p/>
 * {@code @description}  :Redis消息监听
 * <p/>
 * <b>@create:</b> 2023/11/22 15:56.
 *
 * @author zhouxp
 */
@Slf4j
@SuppressWarnings({"unchecked", "rawtypes"})
public class RedisMessageListener implements MessageListener {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedisMessageService messageService;


    @Override
    public void onMessage(Message message, @Nullable byte[] pattern) {

        try {
            //序列化对象（特别注意：发布的时候需要设置序列化；订阅方也需要设置序列化）
            MessageEntity messageBody = (MessageEntity) redisTemplate.getValueSerializer().deserialize(message.getBody());
            if (messageBody != null) {
                messageService.handle(messageBody);
            }else{
                log.info("message info is null");
            }
        } catch (Exception e) {
            log.info("error handle message {}", e.getMessage(), e);
            throw e;
        }


    }
}
