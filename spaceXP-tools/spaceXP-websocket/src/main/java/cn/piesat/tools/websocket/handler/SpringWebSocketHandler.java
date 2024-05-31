package cn.piesat.tools.websocket.handler;

import cn.piesat.framework.common.constants.CommonConstants;
import cn.piesat.tools.websocket.util.SessionManagerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

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
public class SpringWebSocketHandler extends TextWebSocketHandler {
    /**
     * socket 建立成功事件 @OnOpen
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session)  {
        String userId = (String) session.getAttributes().get(CommonConstants.USER_ID);
        // 用户连接成功，放入在线用户缓存
        SessionManagerUtils.add(userId, session);
    }

    /**
     * 接收消息事件 @OnMessage
     *
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 获得客户端传来的消息
        String payload = message.getPayload();
        log.info("server 接收到发送的 {}" , payload);
        session.sendMessage(new TextMessage("server 发送消息 " + payload + " " + LocalDateTime.now()));
    }

    /**
     * socket 断开连接时 @OnClose
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("断开连接");
        String userId = (String) session.getAttributes().get(CommonConstants.USER_ID);
        SessionManagerUtils.removeAndClose(userId);
    }
}
