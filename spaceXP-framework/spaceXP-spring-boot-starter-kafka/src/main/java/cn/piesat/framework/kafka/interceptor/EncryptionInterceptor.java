package cn.piesat.framework.kafka.interceptor;

import cn.piesat.framework.common.utils.AesUtils;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 * <p/>
 * {@code @description}: 加密拦截器
 * <p/>
 * {@code @create}: 2025-01-14 10:05
 * {@code @author}: zhouxp
 */
public class EncryptionInterceptor implements ProducerInterceptor<String,String> {
    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        try {
            String encryptedValue = AesUtils.encrypt(record.value());
            return new ProducerRecord<>(record.topic(), record.partition(), record.timestamp(),
                    record.key(), encryptedValue, record.headers());
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting message", e);
        }
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
}
