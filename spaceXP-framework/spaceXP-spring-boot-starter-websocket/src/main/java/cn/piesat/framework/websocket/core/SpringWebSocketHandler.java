package cn.piesat.framework.websocket.core;

import cn.piesat.framework.common.constants.CommonConstants;
import cn.piesat.framework.websocket.model.MessagePack;
import cn.piesat.framework.websocket.util.SessionSocketHolder;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p/>
 * {@code @description}:
 * <p/>
 * {@code @create}: 2024-06-18 16:31
 * {@code @author}: zhouxp
 */
@Slf4j
public class SpringWebSocketHandler extends AbstractWebSocketHandler {

    @Autowired(required = false)
    private MessageService messageService;
    /**
     * socket 建立成功事件 @OnOpen
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("WebSocket connected 已经建立连接:sessionId={} address={}", session.getId(), Objects.requireNonNull(session.getLocalAddress()));
        try {
            String uid = (String) session.getAttributes().get(CommonConstants.USER_ID);
            Integer appId = (Integer) session.getAttributes().get(CommonConstants.APP_ID);
            if (uid != null || appId != null) {
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
        String payload = message.getPayload();
        try {
            MessagePack messagePack = JSON.parseObject(payload, MessagePack.class);
            if(messageService!=null){
                messageService.handleTextMessage(messagePack);
            }
        }catch (Exception e){
            log.error("处理websocket消息出错:{}",e.getMessage());
        }


    }

    private static void sendTextMessage(WebSocketSession session, TextMessage message) {
        try {
            session.sendMessage(message);
        } catch (Exception e) {
            log.info("WebSocket handleTextMessage 处理文本消息异常: {}", e.getMessage());
            close(session);
        }
    }

    /**
     * 关闭session 并删除资源
     */
    private static void close(WebSocketSession session) {
        try {
            String uid = (String) session.getAttributes().get(CommonConstants.USER_ID);
            Integer appId = (Integer) session.getAttributes().get(CommonConstants.APP_ID);
            if (StringUtils.hasText(uid) && appId != null) {
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

    public void heartbeat(MessagePack messagePack ) {
        ConcurrentHashMap<String, ConcurrentHashMap<Integer, WebSocketSession>> map = SessionSocketHolder.get();
        TextMessage textMessage = new TextMessage(JSON.toJSONString(messagePack));
        map.forEach((k,v)->{
            v.forEach((k1,v1)->{
                sendTextMessage(v1, textMessage);
            });
        }) ;
    }

    public void sendMessage(MessagePack messagePack ) {
        if (messagePack == null || messagePack.getType() == null) {
            log.info("消息或消息类型为空！");
            return;
        }
        ConcurrentHashMap<Integer, WebSocketSession> map = SessionSocketHolder.get(messagePack.getToId());
        TextMessage textMessage = new TextMessage(JSON.toJSONString(messagePack));
        map.forEach((k,v)->{
            sendTextMessage(v, textMessage);
        });
    }
}
