package cn.piesat.kafka.test;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * <p/>
 * {@code @description}:
 * <p/>
 * {@code @create}: 2025-01-13 15:33
 * {@code @author}: zhouxp
 */
@Component
public class KafkaConsumer {

    @KafkaListener(topics = "my-topic", groupId = "my-group")
    public void listen(ConsumerRecord<String, String> message) {
        System.out.println("Received message: " + message);
    }
}
