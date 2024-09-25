package cn.piesat.framework.sse.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p/>
 * {@code @description}: SSE配置类
 * <p/>
 * {@code @create}: 2024-09-25 13:52
 * {@code @author}: zhouxp
 */
@Data
@ConfigurationProperties(prefix = "space.sse")
public class SseProperties {
    /**
     * SSE 超时时间 单位毫秒
     */
    private Long timeout = 0L;
    /**
     * SSE 重连时间 单位毫秒
     */
    private Long reconnectTimeMillis = 60 * 1000L;

    /**
     * 设置线程池大小
     */
    private Integer poolSize = 5;

    /**
     * 心跳间隔 单位毫秒
     */
    private Long heartbeatInterval = 30 * 1000L;

    /**
     * 心跳消息
     */

    private String heartbeatMessage = "ping";
}
