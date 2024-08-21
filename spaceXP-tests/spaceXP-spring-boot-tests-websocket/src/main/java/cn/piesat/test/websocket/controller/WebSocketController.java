package cn.piesat.test.websocket.controller;


import cn.piesat.framework.common.model.entity.MessageEntity;
import cn.piesat.framework.websocket.core.MessageHandler;
import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;

import javax.annotation.Resource;


/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2024-06-11 9:16.
 *
 * @author zhouxp
 */
@RestController
@RequestMapping("/open/socket")
public class WebSocketController {

    @Resource
    private MessageHandler messageHandler;



    @PostMapping(value = "/send")
    public void send()  {

        MessageEntity messageEntity = new MessageEntity(1001L,1001L,1,"hello world","hello");
        TextMessage textMessage = new TextMessage(JSON.toJSONString(messageEntity));
        messageHandler.sendMessage(1001L,textMessage);
    }
}
