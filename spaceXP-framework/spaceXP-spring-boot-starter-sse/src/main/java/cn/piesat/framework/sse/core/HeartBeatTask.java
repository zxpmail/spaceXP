package cn.piesat.framework.sse.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * <p/>
 * {@code @description}: SSE心跳
 * <p/>
 * {@code @create}: 2024-09-25 14:44
 * {@code @author}: zhouxp
 */
@Slf4j
public class HeartBeatTask implements Runnable {
    private final SseEmitter sseEmitter;

    private final String userId;
    private final String appId;

    private final String heartbeatMessage;

    public HeartBeatTask(SseEmitter sseEmitter, String userId, String appId, String heartbeatMessage) {
        this.sseEmitter = sseEmitter;
        this.userId = userId;
        this.appId = appId;
        this.heartbeatMessage = heartbeatMessage;
    }

    @Override
    public void run() {
        log.info("Send Heartbeat Date: {},userId: {},appId: {}", LocalDateTime.now(), userId, appId);
        if (sseEmitter != null) {
            try {
                sseEmitter.send(heartbeatMessage, MediaType.APPLICATION_JSON);
            } catch (IOException e) {
                log.error("Send Heartbeat Message Error  Date: {},userId: {},appId: {}", LocalDateTime.now(), userId, appId, e);
            }
        }
    }
}
