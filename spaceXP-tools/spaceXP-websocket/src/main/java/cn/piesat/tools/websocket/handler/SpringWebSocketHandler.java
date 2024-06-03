package cn.piesat.tools.websocket.handler;

import cn.piesat.framework.common.constants.CommonConstants;
import cn.piesat.tools.websocket.util.SessionManagerUtils;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;


import java.time.LocalDateTime;


/**
 * <p/>
 * {@code @description}  :消息处理器
 * <p/>
 * <b>@create:</b> 2024-05-31 14:21.
 *
 * @author zhouxp
 */
@Component
@Slf4j
public class SpringWebSocketHandler extends AbstractWebSocketHandler {
    /**
     * socket 建立成功事件 @OnOpen
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session)  {
        log.info("WebSocket connected 已经建立连接:sessionId={} address={}", session.getId(), session.getLocalAddress().toString());
        try{
            String uid = session.getAttributes().get(CommonConstants.USER_ID)  + "";
            if(StringUtils.hasText(uid)){
                SessionManagerUtils.add(uid, session);
            }
        }catch(Exception e){
            log.info("WebSocket connected 已经建立连接异常",e);
        }
    }

    /**
     * 接收消息事件 @OnMessage
     *
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)  {
        log.info("WebSocket handleTextMessage 处理文本消息:sessionId={}", session.getId());
        try{
            super.handleMessage(session, message);
        }catch(Exception e){
            log.info("WebSocket handleTextMessage 处理文本消息异常",e);
        }
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        try{
            super.handleMessage(session, message);
        }catch(Exception e){
            log.info("WebSocket handleBinaryMessage异常",e);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception)  {
        afterConnectionClosed(session,CloseStatus.NORMAL);
    }

    /**
     * socket 断开连接时 @OnClose
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status)  {
        log.info("断开连接");
        String uid = session.getAttributes().get(CommonConstants.USER_ID)  + "";
        if(StringUtils.hasText(uid)){
            SessionManagerUtils.removeAndClose(uid);
        }
    }
}
