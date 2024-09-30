package cn.piesat.framework.sse.core;

import cn.piesat.framework.common.exception.BaseException;
import cn.piesat.framework.sse.model.SseAttributes;
import cn.piesat.framework.sse.properties.SseProperties;
import cn.piesat.framework.sse.util.SseSessionHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p/>
 * {@code @description}: Sse处理器
 * <p/>
 * {@code @create}: 2024-09-24 17:51
 * {@code @author}: zhouxp
 */
@Slf4j
public class SseClient {
    @Autowired(required = false)
    private CallbackService callbackService;

    private final SseProperties sseProperties;
    private final AtomicBoolean isCancelled = new AtomicBoolean(false);
    @Resource
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    public SseClient(SseProperties sseProperties) {
        this.sseProperties = sseProperties;
    }

    public SseEmitter createSession(String userId, String appId, Map<String, Object> attributes) {
        if (!StringUtils.hasText(userId) || !StringUtils.hasText(appId)) {
            log.warn("参数异常，用户Id或者应用id为空");
            throw new BaseException("参数异常，用户Id或者应用id为空");
        }
        try {
            SseEmitter sseEmitter = SseSessionHolder.add(userId, appId, sseProperties.getTimeout(), attributes);

            final ScheduledFuture<?> future = threadPoolTaskScheduler.scheduleAtFixedRate(
                    new HeartBeatTask(sseEmitter, userId, appId, sseProperties.getHeartbeatMessage()),
                    sseProperties.getHeartbeatInterval()
            );
            sseEmitter.onCompletion(() -> handleSessionCompletion(future, userId, appId, null));
            sseEmitter.onTimeout(() -> handleSessionCompletion(future, userId, appId, "连接超时...................."));
            sseEmitter.onError(t -> handleSessionCompletion(future, userId, appId, "Error(userId: " + userId + ")"));
            if (callbackService != null) {
                callbackService.addUser2Group(userId, appId, attributes);
            }
            return sseEmitter;
        } catch (Exception e) {
            log.error("创建用户会话出错！具体异常信息: {}", e.getMessage(), e);
            throw new BaseException("创建用户会话出错！");
        }
    }

    private void handleSessionCompletion(ScheduledFuture<?> future, String userId, String appId, String message) {
        synchronized (isCancelled) {
                if (StringUtils.hasText(message)) {
                    SseSessionHolder.onError(userId, appId, new BaseException(message));
                } else {
                    SseSessionHolder.onCompletion(userId, appId);
                }
                sessionClose(userId, appId);
                if (future != null) {
                    future.cancel(true);
                }
        }
    }

    public void sendMessage(String userId, String appId, String messageId, String message, ConcurrentHashMap<String, SseAttributes> userMap) {
        sendMessage(userId, appId, messageId, message, userMap, MediaType.APPLICATION_JSON);
    }

    public void sendMessage(String userId, String appId, String messageId, String message, ConcurrentHashMap<String, SseAttributes> userMap, MediaType mediaType) {
        if (!StringUtils.hasText(userId) || !StringUtils.hasText(appId) || !StringUtils.hasText(messageId) || !StringUtils.hasText(message) || userMap == null || mediaType == null) {
            log.warn("参数异常，用户Id{},应用id{},消息id:{},消息:{}, 媒体类型:{}", userId, appId, messageId, message, mediaType);
            return;
        }
        SseAttributes sseAttributes = userMap.get(appId);
        if (sseAttributes == null) {
            log.warn("参数异常，用户Id{},应用id{},用户会话属性为空", userId, appId);
            return;
        }
        SseEmitter session = sseAttributes.getSession();
        if (session == null) {
            log.warn("参数异常，用户Id{},应用id{},用户会话为空", userId, appId);
            return;
        }
        try {
            session.send(SseEmitter.event().id(messageId).reconnectTime(sseProperties.getReconnectTimeMillis()).data(message, mediaType));
            log.info("用户Id{},应用id{},消息id:{},消息:{}", userId, appId, messageId, message);
        } catch (Exception e) {
            userMap.remove(appId);
            sessionClose(userId, appId);
            log.error("用户Id{},应用id{},消息id:{},消息:{}", userId, appId, messageId, message, e);
            session.complete();
        }
    }

    private void sessionClose(String userId, String appId) {
        if (callbackService != null) {
            callbackService.sessionClose(userId, appId);
        }
    }

    public ConcurrentHashMap<String, SseAttributes> getUserMap(String userId, String appId) {
        return SseSessionHolder.getSessionMap(userId, appId);
    }

    public boolean close(String userId, String appId) {
        if (!StringUtils.hasText(userId) || !StringUtils.hasText(appId)) {
            log.warn("参数异常，用户Id{},应用id{}", userId, appId);
            return false;
        }
        if (SseSessionHolder.onClose(userId, appId)) {
            sessionClose(userId, appId);
        }
        return true;
    }
}
