package cn.piesat.framework.websocket.core;

import cn.piesat.framework.common.constants.CommonConstants;
import cn.piesat.framework.websocket.util.SessionSocketHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import javax.annotation.Nonnull;
import java.util.Objects;

import static cn.piesat.framework.websocket.model.WebsocketConstant.BUFFER_SIZE_LIMIT;
import static cn.piesat.framework.websocket.model.WebsocketConstant.SEND_TIME_LIMIT;

/**
 * <p/>
 * {@code @description}:
 * <p/>
 * {@code @create}: 2024-06-18 16:31
 * {@code @author}: zhouxp
 */
@Slf4j
public class SpringWebSocketHandler extends AbstractWebSocketHandler {

    private final Boolean debugged;
    @Autowired(required = false)
    private CallbackService callbackService;

    @Autowired(required = false)
    private WebSocketMessageService messageService;

    public SpringWebSocketHandler(Boolean debugged) {
        this.debugged = debugged;
    }

    /**
     * socket 建立成功事件 @OnOpen
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("WebSocket connected 已经建立连接:sessionId={} address={}", session.getId(), Objects.requireNonNull(session.getLocalAddress()));
        try {
            Long uid = (Long) session.getAttributes().get(CommonConstants.USER_ID);
            Integer appId = (Integer) session.getAttributes().get(CommonConstants.APP_ID);
            if (uid != null || appId != null) {
                // 实现 session 支持并发，可参考 https://blog.csdn.net/abu935009066/article/details/131218149
                session = new ConcurrentWebSocketSessionDecorator(session, SEND_TIME_LIMIT, BUFFER_SIZE_LIMIT);
                SessionSocketHolder.add(uid, appId, session);
            }
        } catch (Exception e) {
            log.info("WebSocket connected 已经建立连接异常:{}", e.getMessage());
        }
    }

    /**
     * 接收消息事件 @OnMessage
     */
    @Override
    public void handleTextMessage(@Nonnull WebSocketSession session, @Nonnull TextMessage message) {
        try {
            if (messageService != null) {
                messageService.recTextMessage(message);
            }
        } catch (Exception e) {
            log.error("处理websocket消息出错:{}", e.getMessage());
        }


    }

    /**
     * 关闭session 并删除资源
     */
    private  void close(WebSocketSession session) {
        try {
            Long uid = (Long) session.getAttributes().get(CommonConstants.USER_ID);
            Integer appId = (Integer) session.getAttributes().get(CommonConstants.APP_ID);
            if(!debugged) {
                callbackService.sessionClose(uid, appId);
            }
            if (ObjectUtils.isEmpty(uid) && appId != null) {
                SessionSocketHolder.removeAndClose(uid, appId);
            }
        } catch (Exception e) {
            log.error("WebSocket handleBinaryMessage 处理文本消息异常 {}", e.getMessage());
        }


    }

    @Override
    public void handleBinaryMessage(@Nonnull WebSocketSession session, @Nonnull BinaryMessage message) {
        try {
            session.sendMessage(message);
        } catch (Exception e) {
            log.info("WebSocket handleBinaryMessage 处理文本消息异常:{}", e.getMessage());
            close(session);
        }
    }

    @Override
    public void handleTransportError(@Nonnull WebSocketSession session, @Nonnull Throwable exception) {
        log.error("处理数据错误:{}", exception.getMessage());
        close(session);
    }

    /**
     * socket 断开连接时 @OnClose
     */
    @Override
    public void afterConnectionClosed(@Nonnull WebSocketSession session, @Nonnull CloseStatus status) {
        log.info("断开连接");
        close(session);
    }
}
