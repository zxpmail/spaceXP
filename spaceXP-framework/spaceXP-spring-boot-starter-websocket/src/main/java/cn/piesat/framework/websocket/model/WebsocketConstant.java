package cn.piesat.framework.websocket.model;

/**
 * <p/>
 * {@code @description}: 常量类
 * <p/>
 * {@code @create}: 2024-06-19 10:32
 * {@code @author}: zhouxp
 */
public interface WebsocketConstant {
    String DEBUG_USER_ID = "1001";
    Integer DEBUG_USER_APPID = 0;

    String DEFAULT_DOMAIN = "*";

    /**
     * 发送时间的限制，单位：毫秒
     */
    Integer SEND_TIME_LIMIT = 1000 * 5;
    /**
     * 发送消息缓冲上线，单位：bytes
     */
    Integer BUFFER_SIZE_LIMIT = 1024 * 100;
}
