package cn.piesat.framework.websocket.core;

import cn.piesat.framework.websocket.util.SessionSocketHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p/>
 * {@code @description}:
 * <p/>
 * {@code @create}: 2024-06-20 14:39
 * {@code @author}: zhouxp
 */
@Slf4j
public class MessageHandler {

    @Autowired(required = false)
    private CallbackService callbackService;

    public void heartbeat(TextMessage message) {
        ConcurrentHashMap<Long, ConcurrentHashMap<Integer, WebSocketSession>> map = SessionSocketHolder.get();
        if (map == null) {
            return;
        }
        for (Map.Entry<Long, ConcurrentHashMap<Integer, WebSocketSession>> e : map.entrySet()) {
            for (Map.Entry<Integer, WebSocketSession> e1 : e.getValue().entrySet()) {
                WebSocketSession session = e1.getValue();
                if (session.isOpen()) {
                    try {
                        session.sendMessage(message);
                    } catch (Exception ex) {
                        log.error("Failed to send heartbeat to session: " + session.getId(), ex);
                    }
                } else {
                    if (callbackService != null) {
                        callbackService.sessionClose(e.getKey(), e1.getKey());
                    }
                    e.getValue().remove(e1.getKey());
                }
            }
        }
    }

    public void sendMessage(Long toUserId, TextMessage message) {
        if (ObjectUtils.isEmpty(toUserId) || ObjectUtils.isEmpty(message)) {
            log.info("消息或消息类型为空！");
            return;
        }
        ConcurrentHashMap<Integer, WebSocketSession> map = SessionSocketHolder.get(toUserId);
        if (map == null) {
            return;
        }
        for (Map.Entry<Integer, WebSocketSession> entry : map.entrySet()) {
            WebSocketSession session = entry.getValue();
            if (session.isOpen()) {
                try {
                    session.sendMessage(message);
                } catch (Exception ex) {
                    log.error("Failed to send heartbeat to session: " + session.getId(), ex);
                }
            } else {
                if (callbackService != null) {
                    callbackService.sessionClose(toUserId, entry.getKey());
                }
                map.remove(entry.getKey());
            }
        }
    }
}
