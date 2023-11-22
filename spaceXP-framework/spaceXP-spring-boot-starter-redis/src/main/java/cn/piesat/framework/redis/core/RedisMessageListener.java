package cn.piesat.framework.redis.core;

import cn.piesat.framework.redis.model.MessageBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

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
public class RedisMessageListener implements MessageListener {

    @Resource
    private  RedisTemplate<String,Object> redisTemplate;

    @Resource
    private  MessageService messageService;



    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 接收的topic
        log.info("channel:" + new String(pattern));

        //序列化对象（特别注意：发布的时候需要设置序列化；订阅方也需要设置序列化）
        MessageBody messageBody = (MessageBody) redisTemplate.getValueSerializer().deserialize(message.getBody());
        messageService.handle(messageBody);
    }
}
