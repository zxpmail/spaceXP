package cn.piesat.framework.kafka.datasource.properties;

import cn.piesat.framework.kafka.datasource.constant.Constant;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p/>
 * {@code @description}  : kafka配置类
 * <p/>
 * <b>@create:</b> 2024-03-21 10:40.
 *
 * @author zhouxp
 */
@Data
@ConfigurationProperties(prefix = "space.kafka")
public class KafkaConfigProperties {
    @NestedConfigurationProperty
    private Map<String, KafkaConfig> servers = new LinkedHashMap<>();


    @Data
    @Slf4j
    @Accessors(chain = true)
    public static class KafkaConfig {

        /**
         * 生产者默认值
         */
        private Map<String, Object> producer =new HashMap<String, Object>(){
            {
                put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class);
                put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class);
                put(ProducerConfig.ACKS_CONFIG, Constant.ALL);
                put(ProducerConfig.BATCH_SIZE_CONFIG, Constant.BATCH_SIZE);
                put(ProducerConfig.BUFFER_MEMORY_CONFIG,Constant.BUFFER_MEMORY);
            }
        };

        /**
         * 消费者默认值
         */
        private Map<String, Object> consumer = new LinkedHashMap<String, Object>() {
            {
                put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class);
                put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class);
                put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, Constant.AUTO_OFFSET_RESET);
            }
        };
        /**
         *  消费者工厂配置
         */
        private Map<String, ConsumerExtraConfig> consumerExtra = new LinkedHashMap<>();

        /**
         * 生产端需要单独设置beanName
         */
        private String producerBeanName;
    }

    /**
     * 消费者扩展实体类
     */
    @Data
    public static class ConsumerExtraConfig {
        /**
         * 是否可以丢弃消息，默认false
         */
        private Boolean ackDiscarded = false;

        /**
         * 消费者自定义消费工厂
         */
        private Class<? extends KafkaListenerContainerFactory<?>> consumerFactory;

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
        private ConsumerFactoryFilter factoryFilter;
    }

    /**
     * 工厂过滤实体
     */
    @SuppressWarnings("rawtypes")
    @Data
    public static class ConsumerFactoryFilter{
        /**
         * 要序列化成的对象
         */
        private Class<?> serializerClass;
        /**
         * 指定自定义策略
         */
        private Class<? extends RecordFilterStrategy> strategy;
        /**
         * 过滤的字段集合，key为要对比的字段，value为比较的值，支持逗号分隔
         */
        private Map<String, String> filter = new LinkedHashMap<>();
    }
}
