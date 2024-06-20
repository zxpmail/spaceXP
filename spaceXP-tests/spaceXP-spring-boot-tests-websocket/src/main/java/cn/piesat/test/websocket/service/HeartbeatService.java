package cn.piesat.test.websocket.service;


import cn.piesat.framework.websocket.core.MessageHandler;
import cn.piesat.framework.websocket.model.MessagePack;
import com.alibaba.fastjson.JSON;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * <p/>
 * {@code @description}: 心跳服务类
 * <p/>
 * {@code @create}: 2024-06-19 17:28
 * {@code @author}: zhouxp
 */
@Service
@EnableScheduling
public class HeartbeatService {
    @Resource
    private MessageHandler messageHandler;

    @Scheduled(fixedDelay = 360,timeUnit = TimeUnit.HOURS)
    public void run(){
        MessagePack messagePack = new MessagePack(0,"1001","1001",1," heartbeat");
        TextMessage textMessage = new TextMessage(JSON.toJSONString(messagePack));
        messageHandler.heartbeat(textMessage);
    }
}
