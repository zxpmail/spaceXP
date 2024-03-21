package cn.piesat.framework.kafka.datasource.model;

import cn.piesat.framework.kafka.datasource.constant.Constant;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p/>
 * {@code @description}  :消费参数默认配置
 * <p/>
 * <b>@create:</b> 2024-03-21 9:57.
 *
 * @author zhouxp
 */
@Data
@Slf4j
@Accessors(chain = true)
public class DataSourceEntity {

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
    private Map<String, ExtraEntity> consumerExtra = new LinkedHashMap<>();

    /**
     * 生产者工厂配置
     */
    private Map<String, ExtraEntity> producerExtra = new LinkedHashMap<>();

    /**
     * 生产端需要单独设置beanName
     */
    private String producerBeanName;
}
