package cn.piesat.framework.kafka.datasource.model;

import lombok.Data;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.GenericMessageListenerContainer;

/**
 * <p/>
 * {@code @description}  : 扩展实体类
 * <p/>
 * <b>@create:</b> 2024-03-21 10:24.
 *
 * @author zhouxp
 */
@Data
public class ExtraEntity {

    /**
     * 是否可以丢弃消息，默认false
     */
    private Boolean ackDiscarded = false;

    /**
     * 消费者自定义消费工厂
     */
    private Class<? extends GenericMessageListenerContainer<?,?>> consumerFactory;

    /**
     * 消息拉取超时时间
     */
    private long pollTimeout = 3000;

    /**
     * 批量监听
     */
    private Boolean batchListener = false;

    /**
     * KafkaMessageListenerContainer 实例数量
     */
    private Integer concurrency = 1;

    /**
     * 消费过滤工厂配置
     */
    private FactoryFilterEntity consumerFactoryFilter;

    /**
     * 自定义生产工厂
     */
    private Class<? extends ProducerFactory<?,?>> producerFactory;
}
