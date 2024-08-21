package cn.piesat.test.websocket.service;



import cn.piesat.framework.common.model.entity.MessageEntity;
import cn.piesat.framework.websocket.core.MessageHandler;
import cn.piesat.framework.websocket.core.WebSocketMessageService;


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
public class MessageServiceImpl implements WebSocketMessageService {

    @Resource
    private MessageHandler messageHandler;
    @Override
    public void recTextMessage(TextMessage message) {
        String payload = message.getPayload();
        if(StringUtils.hasText(payload)) {
            MessageEntity messageEntity = JSON.parseObject(payload, MessageEntity.class);
            messageHandler.sendMessage(messageEntity.getToId(), message);
        }
    }
}
