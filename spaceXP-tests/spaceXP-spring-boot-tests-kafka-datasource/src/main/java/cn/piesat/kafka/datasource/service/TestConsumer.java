package cn.piesat.kafka.datasource.service;

import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2024-03-21 15:22.
 *
 * @author zhouxp
 */
@EnableKafka
@Component
@DependsOn("kafkaCreator")//必须，不然出错
public class TestConsumer {

    @KafkaListener(topics = {"test10"}, containerFactory = "consumer10", groupId = "group")
    public void listener10(String data) {
        System.out.println("msg is==================" + data);
    }

    @KafkaListener(topics = {"test11"}, containerFactory = "consumer11", groupId = "group")
    public void listener11(String data) {
        System.out.println("msg is==================" + data);
    }

    @KafkaListener(topics = {"test20"}, containerFactory = "consumer20", groupId = "group")
    public void listener20(String data) {
        System.out.println("msg is==================" + data);
    }


}
