package cn.piesat.kafka.config;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * <p/>
 * {@code @description}:
 * <p/>
 * {@code @create}: 2024-10-18 15:18
 * {@code @author}: zhouxp
 */

//@Configuration
//@EnableKafka
public class KafkaConfig {

    private static final String kafkaServer = "192.168.2.32:9092";//kafka地址


    /**
     * @Title producerFactory
     * @Description TODO 生产者工厂类，设置生产者相关配置
     * @return org.springframework.kafka.core.ProducerFactory<java.lang.String,java.lang.Object>
     */
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);//kafka 地址
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);//序列化
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);//序列化
        props.put(ProducerConfig.ACKS_CONFIG, "all");//确认机制，all是所有副本确认，1是一个副本确认，0是不需要副本确认
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, "10");//批量发送大小
        props.put(ProducerConfig.LINGER_MS_CONFIG, "1");//批量发送等待时间  和上面的batch-size谁先到先发送
        return new DefaultKafkaProducerFactory<>(props);
    }

    /**
     * @Title kafkaTemplate
     * @Description TODO kafka生产者工具类
     * @return org.springframework.kafka.core.KafkaTemplate<java.lang.String,java.lang.Object>
     */
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }



    /**
     * @Title consumerFactory
     * @Description TODO 消费者工厂类，配置消费者的一些配置
     * @return org.springframework.kafka.core.ConsumerFactory<java.lang.String,java.lang.Object>
     */
    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, 5 * 1024 * 1024);//每次抓取消息的大小
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);//是否自动提交
        props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 50 * 1000 * 1000);//请求超时时间
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 60000000);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);

    }

    /**
     * @Title kafkaListenerContainerFactory
     * @Description TODO 监听容器的工厂类，创建监听容器时使用
     * @return org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory<java.lang.String,java.lang.Object>
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    /**
     * @Title adminClient
     * @Description TODO kafka客户端
     * @return org.apache.kafka.clients.admin.AdminClient
     */
    @Bean
    public AdminClient adminClient() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return AdminClient.create(props);
    }
}
