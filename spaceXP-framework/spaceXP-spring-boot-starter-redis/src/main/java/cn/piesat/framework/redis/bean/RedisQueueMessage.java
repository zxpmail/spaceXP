package cn.piesat.framework.redis.bean;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p/>
 * {@code @description}: redis队列消息
 * <p/>
 * {@code @create}: 2025-02-27 09:46:50
 * {@code @author}: zhouxp
 */
@Data
public class RedisQueueMessage {
    /**
     * 消息唯一标识
     */
    private String id;
    /**
     * 消息的分类 传入Spring BeanName
     * 为消费时不同类去处理
     */
    private String beanName;
    /**
     * 具体消息 json
     */
    private String body;

    /**
     * 延时时间 被消费时间  取当前时间戳 延迟时间
     */
    private Long delayTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
