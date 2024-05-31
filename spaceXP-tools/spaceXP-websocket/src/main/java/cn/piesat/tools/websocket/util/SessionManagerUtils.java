package cn.piesat.tools.websocket.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p/>
 * {@code @description}  :Session管理器工具
 * <p/>
 * <b>@create:</b> 2024-05-31 13:31.
 *
 * @author zhouxp
 */

public class SessionManagerUtils {


    /**
     * 保存连接 session 的地方
     */
    private static final ConcurrentHashMap<String, WebSocketSession> SESSION_POOL = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(SessionManagerUtils.class);

    /**
     * 添加 session
     */
    public static void add(String key, WebSocketSession session) {
        if (key == null || session == null) {
            logger.warn("Attempted to add a null key or session to the pool.");
            return;
        }
        WebSocketSession tempSession = SESSION_POOL.get(key);
        if (tempSession != null) {
            try {
                session.close();
            } catch (IOException e) {
                logger.error("Failed to close WebSocket session with key: " + key, e);
            }
        }
        SESSION_POOL.put(key, session);
    }

    /**
     * 删除 session,会返回删除的 session
     */
    public static WebSocketSession remove(String key) {
        if (key == null) {
            logger.warn("Attempted to remove a session with a null key.");
            return null;
        }
        return SESSION_POOL.remove(key);
    }

    /**
     * 删除并同步关闭连接
     */
    public static void removeAndClose(String key) {
        close(key,remove(key));
    }

    /**
     * 释放session资源
     */
    public static  void close(String key,WebSocketSession session){
        if (session == null) {
            logger.warn("Attempted to get a session with a null");
            return ;
        }
        try {
            session.close();
        } catch (IOException e) {
            logger.error("Failed to close WebSocket session with key: " + key, e);
        }
    }
    /**
     * 获得 session
     */
    public static WebSocketSession get(String key) {
        if (key == null) {
            logger.warn("Attempted to get a session with a null key.");
            return null;
        }
        return SESSION_POOL.get(key);
    }
}
