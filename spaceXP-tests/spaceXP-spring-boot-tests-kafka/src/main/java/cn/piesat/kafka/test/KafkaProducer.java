package cn.piesat.kafka.test;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * <p/>
 * {@code @description}:
 * <p/>
 * {@code @create}: 2025-01-13 15:29
 * {@code @author}: zhouxp
 */
@Component
public class KafkaProducer {

    @Setter(onMethod_ = @Autowired )
    private KafkaTemplate<String, String> kafkaTemplate;



    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }
}
