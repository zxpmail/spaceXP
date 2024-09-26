package cn.piesat.tests.sse.controller;


import cn.piesat.framework.common.annotation.NoApiResult;
import cn.piesat.framework.common.model.entity.MessageEntity;
import cn.piesat.framework.redis.core.RedisService;
import cn.piesat.framework.sse.core.SseClient;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import java.time.LocalDateTime;


/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2024-06-11 9:16.
 *
 * @author zhouxp
 */
@RestController
@RequestMapping("/sse")
@CrossOrigin
public class SseTestController {

    @Resource
    private SseClient sseClient;
    @Resource
    private RedisService redisService;

    @GetMapping("/sendMessage")
    public void sendMessage() {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setAppId(1);
        messageEntity.setToId(1001L);
        messageEntity.setFromId(1L);
        messageEntity.setType(1);
        messageEntity.setTitle("hello");
        messageEntity.setBody("hello world " + LocalDateTime.now());
        redisService.convertAndSend("TOPIC", messageEntity);
    }

    @NoApiResult
    @PostMapping(value = "/login")
    public SseEmitter login(String userId, String appId) {
        return sseClient.createSession(userId,appId);
    }
}
