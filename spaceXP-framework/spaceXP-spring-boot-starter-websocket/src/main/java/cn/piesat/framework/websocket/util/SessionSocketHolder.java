package cn.piesat.framework.websocket.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p/>
 * {@code @description}: Session管理工具
 * 一个用户可以多端登录websocket如：pc、phone等不同设备
 * <p/>
 * {@code @create}: 2024-06-18 15:40
 * {@code @author}: zhouxp
 */

@Slf4j
public class SessionSocketHolder {
    private static final ConcurrentHashMap<Long, ConcurrentHashMap<Integer, WebSocketSession>> SESSION_POOL = new ConcurrentHashMap<>();

    /**
     * 添加 session
     */
    public static void add(Long userId, Integer appId, WebSocketSession session) {
        if (userId == null || session == null || appId == null) {
            log.warn("Attempted to add a null key or session to the pool.");
            return;
        }
        ConcurrentHashMap<Integer, WebSocketSession> userMap = SESSION_POOL.get(userId);
        if (userMap != null) {
            WebSocketSession userSession = userMap.get(appId);
            if (userSession != null) {
                try {
                    userSession.close();
                    userMap.remove(appId);
                } catch (IOException e) {
                    log.error("Failed to close WebSocket session with user:{},{},{} ", userId, appId, e);
                }
            }
            userMap.put(appId, session);
        } else {
            ConcurrentHashMap<Integer, WebSocketSession> user = new ConcurrentHashMap<>();
            user.put(appId, session);
            SESSION_POOL.put(userId, user);
        }

    }

    /**
     * 删除 session,会返回删除的 session
     */
    public static WebSocketSession remove(Long userId, Integer appId) {
        ConcurrentHashMap<Integer, WebSocketSession> sessionMap = getSessionMap(userId, appId);
        if (sessionMap == null) return null;
        return sessionMap.remove(appId);
    }

    private static ConcurrentHashMap<Integer, WebSocketSession> getSessionMap(Long userId, Integer appId) {
        if (userId == null || appId == null) {
            log.info("Attempted to get a session with a null key.");
            return null;
        }
        ConcurrentHashMap<Integer, WebSocketSession> sessionMap = SESSION_POOL.get(userId);
        if (CollectionUtils.isEmpty(sessionMap)) {
            log.info("There does not exist a session containing the userId:{} ", userId);
            return null;
        }
        return sessionMap;
    }

    /**
     * 删除并同步关闭连接
     */
    public static void removeAndClose(Long userId, Integer appId) {
        close(userId, appId,remove(userId,appId));
    }

    /**
     * 释放session资源
     */
    public static void close(Long userId, Integer appId, WebSocketSession session) {
        if (session == null) {
            log.warn("Attempted to get a session with a null");
            return;
        }
        try {
            session.close();
        } catch (IOException e) {
            log.error("Failed to close WebSocket session with user:{} ,{}，{}", userId, appId, e);
        }
    }

    public static ConcurrentHashMap<Long, ConcurrentHashMap<Integer, WebSocketSession>>get(){
        return SESSION_POOL;
    }

    public static ConcurrentHashMap<Integer, WebSocketSession>get( Long userId){
        return SESSION_POOL.get(userId);
    }
}
