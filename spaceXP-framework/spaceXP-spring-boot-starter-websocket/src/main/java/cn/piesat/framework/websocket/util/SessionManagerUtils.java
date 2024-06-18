package cn.piesat.framework.websocket.util;

import cn.piesat.framework.websocket.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p/>
 * {@code @description}: Session管理工具
 * <p/>
 * {@code @create}: 2024-06-18 15:40
 * {@code @author}: zhouxp
 */

@Slf4j
public class SessionManagerUtils {
    private static final ConcurrentHashMap<User<?>, WebSocketSession> SESSION_POOL = new ConcurrentHashMap<>();

    /**
     * 添加 session
     */
    public static void add(User<?> user, WebSocketSession session) {
        if (user == null || session == null) {
            log.warn("Attempted to add a null key or session to the pool.");
            return;
        }
        WebSocketSession tempSession = SESSION_POOL.get(user);
        if (tempSession != null) {
            try {
                session.close();
            } catch (IOException e) {
                log.error("Failed to close WebSocket session with user:{},{} ", user, e);
            }
        }
        SESSION_POOL.put(user, session);
    }

    /**
     * 删除 session,会返回删除的 session
     */
    public static WebSocketSession remove(User<?> user) {
        if (user == null) {
            log.warn("Attempted to remove a session with a null key.");
            return null;
        }
        return SESSION_POOL.remove(user);
    }

    /**
     * 删除并同步关闭连接
     */
    public static void removeAndClose(User<?> user) {
        close(user, remove(user));
    }

    /**
     * 释放session资源
     */
    public static void close(User<?> user, WebSocketSession session) {
        if (session == null) {
            log.warn("Attempted to get a session with a null");
            return;
        }
        try {
            session.close();
        } catch (IOException e) {
            log.error("Failed to close WebSocket session with user:{} ,{}", user, e);
        }
    }

    /**
     * 获得 session
     */
    public static WebSocketSession get(User<?> user) {
        if (user == null) {
            log.warn("Attempted to get a session with a null key.");
            return null;
        }
        return SESSION_POOL.get(user);
    }

}
