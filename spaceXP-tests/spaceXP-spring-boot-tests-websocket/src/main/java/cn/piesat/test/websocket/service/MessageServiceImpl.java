package cn.piesat.test.websocket.service;



import cn.piesat.framework.websocket.core.MessageHandler;
import cn.piesat.framework.websocket.core.MessageService;

import cn.piesat.test.websocket.model.MessagePack;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.TextMessage;

import javax.annotation.Resource;


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
        if(StringUtils.hasText(payload)) {
            MessagePack messagePack = JSON.parseObject(payload, MessagePack.class);
            messageHandler.sendMessage(messagePack.getToId(), message);
        }
    }
}
