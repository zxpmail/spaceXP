package cn.piesat.kafka.test;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.record.Record;
import org.checkerframework.checker.units.qual.K;

import java.util.Map;


/**
 * <p/>
 * {@code @description}:
 * <p/>
 * {@code @create}: 2025-01-13 17:09
 * {@code @author}: zhouxp
 */
public class MyProducerInterceptor<K,V> implements ProducerInterceptor<K, V> {
    @Override
    public ProducerRecord<K, V> onSend(ProducerRecord<K, V> record) {
        System.out.println("拦截器执行");
        V value = record.value();
        if(value instanceof String){
            if ("hello".equals(value)) {
                String v = "你好";

                return new ProducerRecord<>(
                        record.topic(),
                        record.partition(),
                        record.timestamp(),
                        record.key(),
                        (V) v,  // 修改后的 value值
                        record.headers());
            }
        }
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {

    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
/*    @Override
    public ProducerRecord onSend(ProducerRecord record) {
        System.out.println("拦截器执行");

        String value = (String) record.value();
        if ("hello".equals(value)) {
            value = "你好";
        }
        ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(
                record.topic(),
                record.partition(),
                record.timestamp(),
                (String) record.key(),
                value,  // 修改后的 value值
                record.headers());
        return producerRecord;

    }*/

}
