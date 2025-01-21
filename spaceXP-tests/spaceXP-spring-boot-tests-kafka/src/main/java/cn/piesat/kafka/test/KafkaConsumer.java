package cn.piesat.kafka.test;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p/>
 * {@code @description}:
 * <p/>
 * {@code @create}: 2025-01-13 15:33
 * {@code @author}: zhouxp
 */
@Component
public class KafkaConsumer {

    @KafkaListener(topics = "parse_telemetry_real_time_topic", groupId = "my-group",concurrency = "5")
    public void listen(List<ConsumerRecord<String, String>> messages) {
        for (ConsumerRecord<String, String> message : messages) {
            System.out.println("Received message: " + message.value());
        }

    }

    @KafkaListener(topics = "parse_telemetry_real_time_topic", groupId = "my-group1")
    public void listen1(List<ConsumerRecord<String, String>> messages) {
        for (ConsumerRecord<String, String> message : messages) {
            System.out.println("Received message: " + message.value());
        }

    }
}
