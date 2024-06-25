package cn.piesat.test.websocket.service;



import cn.piesat.framework.websocket.core.MessageHandler;
import cn.piesat.framework.websocket.core.MessageService;

import cn.piesat.test.websocket.model.MessagePack;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;




/**
 * <p/>
 * {@code @description}:
 * <p/>
 * {@code @create}: 2024-06-19 17:21
 * {@code @author}: zhouxp
 */
@Service
public class MessageServiceImpl implements MessageService{

    @Resource
    private MessageHandler messageHandler;
    @Override
    public void recTextMessage(TextMessage message) {
        String payload = message.getPayload();
        MessagePack messagePack = JSON.parseObject(payload, MessagePack.class);
        messageHandler.sendMessage(messagePack.getToId(),message);
    }
}
