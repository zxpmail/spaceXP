package cn.piesat.framework.sse.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p/>
 * {@code @description}: Sse会话管理器
 * 一个用户可以多端登录sse如：pc、phone等不同设备
 * <p/>
 * {@code @create}: 2024-09-24 17:40
 * {@code @author}: zhouxp
 */
@Slf4j
public class SseSessionHolder {
    private static final ConcurrentHashMap<Long, ConcurrentHashMap<Integer, SseEmitter>> SESSION_POOL = new ConcurrentHashMap<>();

    /**
     * 添加 session
     */
    public static void add(Long userId, Integer appId, SseEmitter session) {
        if (userId == null || session == null || appId == null) {
            log.warn("Attempted to add a null key or session to the pool.");
            return;
        }
        ConcurrentHashMap<Integer, SseEmitter> userMap = SESSION_POOL.computeIfAbsent(userId, k -> new ConcurrentHashMap<>());

        SseEmitter userSession = userMap.get(appId);
        if (userSession != null) {
            log.info("Date: {} | Client: {} | AppId: {} | Reconnection to server", new Date(), userId, appId);
        } else {
            userMap.put(appId, session);
            log.info("Date: {} | Client: {} | AppId: {} | Connection to server", new Date(), userId, appId);
        }
    }

    /**
     * 删除 session,会返回删除的 session
     */
    public static SseEmitter remove(Long userId, Integer appId) {
        ConcurrentHashMap<Integer, SseEmitter> sessionMap = getSessionMap(userId, appId);
        if (sessionMap == null) return null;
        return sessionMap.remove(appId);
    }

    private static ConcurrentHashMap<Integer, SseEmitter> getSessionMap(Long userId, Integer appId) {
        if (userId == null || appId == null) {
            log.warn("Invalid input is null. userId: {}, appId: {}",
                    userId, appId);
            return null;
        }
        ConcurrentHashMap<Integer, SseEmitter> sessionMap = SESSION_POOL.get(userId);
        if (CollectionUtils.isEmpty(sessionMap)) {
            log.info("There does not exist a session containing the userId:{} ", userId);
            return null;
        }
        return sessionMap;
    }

    public static ConcurrentHashMap<Long, ConcurrentHashMap<Integer, SseEmitter>> get() {
        return SESSION_POOL;
    }

    public static ConcurrentHashMap<Integer, SseEmitter> get(Long userId) {
        return SESSION_POOL.get(userId);
    }
}
