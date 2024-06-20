package cn.piesat.framework.websocket.core;

import org.springframework.web.socket.TextMessage;


/**
 * <p/>
 * {@code @description}: 收到消息处理
 * <p/>
 * {@code @create}: 2024-06-19 10:56
 * {@code @author}: zhouxp
 */
public interface MessageService {
    /**
     * 用户收到消息处理进行处理
     */
    default  void recTextMessage(TextMessage message){
    }
}
