package cn.piesat.framework.sse.util;

import cn.piesat.framework.common.exception.BaseException;
import cn.piesat.framework.common.model.dto.TwoDTO;
import cn.piesat.framework.sse.model.SseAttributes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

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
    private static final ConcurrentHashMap<String, ConcurrentHashMap<String, SseAttributes>> SESSION_POOL = new ConcurrentHashMap<>();

    /**
     * 增加用户会话功能
     *
     * @param userId  用户Id
     * @param appId   应用ID
     * @param timeout 超时时间
     * @return 用户会话
     */
    public static SseEmitter add(String userId, String appId, Long timeout, Map<String, Object> attributes) {
        if (!StringUtils.hasText(userId) || !StringUtils.hasText(appId)) {
            log.warn("Date: {} Attempted to add a null key or session to the pool.", LocalDateTime.now());
            throw new BaseException("userId or appId is null");
        }
        ConcurrentHashMap<String, SseAttributes> userMap = SESSION_POOL.computeIfAbsent(userId, k -> new ConcurrentHashMap<>());

        SseAttributes sseAttributes = userMap.get(appId);
        SseEmitter userSession = null;
        if (sseAttributes != null) {
            userSession = sseAttributes.getSession();
            sseAttributes.setAttributes(attributes);
        }
        if (userSession != null) {
            log.info("Date: {} | Client: {} | AppId: {} | Reconnection to server", LocalDateTime.now(), userId, appId);
        } else {
            try {
                userSession = new SseEmitter(timeout);
                sseAttributes = new SseAttributes();
                sseAttributes.setAttributes(attributes);
                sseAttributes.setSession(userSession);
                userMap.put(appId, sseAttributes);
                log.info("Date: {} | Client: {} | AppId: {} | Connection to server", LocalDateTime.now(), userId, appId);
            } catch (Exception e) {
                log.error("Date: {}  create user session error", LocalDateTime.now(), e);
                throw new BaseException("create user session error");
            }
        }
        return userSession;
    }

    /**
     * 删除 session,会返回删除的 session
     */
    public static void remove(String userId, String appId) {
        ConcurrentHashMap<String, SseAttributes> sessionMap = getSessionMap(userId, appId);
        if (sessionMap == null) {
            log.error("Date: {}  session is not exists", LocalDateTime.now());
            throw new BaseException("session is not exists");
        }
        sessionMap.remove(appId);
    }

    public static void onCompletion(String userId, String appId) {
        remove(userId, appId);
        log.info("Date: {} userId:{} appId:{}  finish connect...................", LocalDateTime.now(), userId, appId);
    }

    private static TwoDTO<SseEmitter, ConcurrentHashMap<String, SseAttributes>> validateEmitter(String userId, String appId) {
        ConcurrentHashMap<String, SseAttributes> sessionMap = getSessionMap(userId, appId);
        if (sessionMap == null) {
            return null;
        }
        SseAttributes attributes = sessionMap.get(appId);
        if (attributes == null) {
            return null;
        }
        return new TwoDTO<>(attributes.getSession(), sessionMap);
    }

    private static void removeEmitterFromMap(ConcurrentHashMap<String, SseAttributes> map, String appId) {
        if (map != null) {
            map.remove(appId);
        }
    }

    public static boolean onClose(String userId, String appId) {
        TwoDTO<SseEmitter, ConcurrentHashMap<String, SseAttributes>> sseEmitterConcurrentHashMapTwoDTO = validateEmitter(userId, appId);
        if (sseEmitterConcurrentHashMapTwoDTO == null || sseEmitterConcurrentHashMapTwoDTO.getFirst() == null) {
            return false;
        }
        try {
            SseEmitter sseEmitter = sseEmitterConcurrentHashMapTwoDTO.getFirst();
            sseEmitter.complete();

            removeEmitterFromMap(sseEmitterConcurrentHashMapTwoDTO.getSecond(), appId);

            log.info("Date: {} userId:{} appId:{}  session close...................", LocalDateTime.now(), userId, appId);
        } catch (Exception e) {
            log.error("Failed to close session for userId:{} appId:{}", userId, appId, e);
            return false;
        }
        return true;
    }

    public static void onError(String userId, String appId, BaseException e) {
        TwoDTO<SseEmitter, ConcurrentHashMap<String, SseAttributes>> sseEmitterConcurrentHashMapTwoDTO = validateEmitter(userId, appId);
        if (sseEmitterConcurrentHashMapTwoDTO == null || sseEmitterConcurrentHashMapTwoDTO.getFirst() == null) {
            return;
        }
        try {
            SseEmitter sseEmitter = sseEmitterConcurrentHashMapTwoDTO.getFirst();
            if (sseEmitter != null) {
                log.error("Date: {} userId:{} appId:{}  {}...................", LocalDateTime.now(), userId, appId, e.getMessage());
                sseEmitter.completeWithError(e);
            }
            removeEmitterFromMap(sseEmitterConcurrentHashMapTwoDTO.getSecond(), appId);
        } catch (Exception ex) {
            log.error("Failed to close session for userId:{} appId:{}", userId, appId, ex);
        }
    }

    /**
     * 根据userId和 appId获取用户sessionMap
     *
     * @param userId 用户ID
     * @param appId  应用ID
     * @return 用户sessionMap
     */
    public static ConcurrentHashMap<String, SseAttributes> getSessionMap(String userId, String appId) {
        if (userId == null || appId == null) {
            log.warn("Date:{} Invalid input userId or appId is null", LocalDateTime.now());
            return null;
        }
        ConcurrentHashMap<String, SseAttributes> sessionMap = SESSION_POOL.get(userId);
        if (CollectionUtils.isEmpty(sessionMap)) {
            log.info("There does not exist a session containing the userId:{} ", userId);
            return null;
        }
        return sessionMap;
    }

    /**
     * 根据用户ID和应用ID获取用户会话
     *
     * @param userId 用户ID
     * @param appId  应用Id
     * @return 用户会话
     */
    public static SseAttributes getSession(String userId, String appId) {
        ConcurrentHashMap<String, SseAttributes> sessionMap = getSessionMap(userId, appId);
        if (sessionMap != null) {
            return sessionMap.get(appId);
        }
        return null;
    }

    public static ConcurrentHashMap<String, ConcurrentHashMap<String, SseAttributes>> get() {
        return SESSION_POOL;
    }

    public static ConcurrentHashMap<String, SseAttributes> get(String userId) {
        return SESSION_POOL.get(userId);
    }
}
