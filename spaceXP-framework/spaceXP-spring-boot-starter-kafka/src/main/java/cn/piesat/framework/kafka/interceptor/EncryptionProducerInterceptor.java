package cn.piesat.framework.kafka.interceptor;

import cn.piesat.framework.common.utils.AesUtils;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static cn.piesat.framework.kafka.constants.KafkaConstant.ENCRYPTION_TOPICS;


/**
 * <p/>
 * {@code @description}: 加密拦截器
 * <p/>
 * {@code @create}: 2025-01-14 10:05
 * {@code @author}: zhouxp
 */
public class EncryptionProducerInterceptor implements ProducerInterceptor<String, String> {


    private final Set<String> encryptionTopics = new HashSet<>();

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        if (encryptionTopics.isEmpty()) {
            return record;
        }

        for (String encryptionTopic : encryptionTopics) {
            if (antPathMatcher.match(encryptionTopic, record.topic())) {
                try {
                    String encryptedValue = AesUtils.encrypt(record.value());
                    if (encryptedValue == null || encryptedValue.isEmpty()) {
                        throw new IllegalArgumentException("Encryption result is empty or null");
                    }
                    return new ProducerRecord<>(record.topic(), record.partition(), record.timestamp(),
                            record.key(), encryptedValue, record.headers());
                } catch (Exception e) {
                    throw new EncryptionException("Error encrypting message", e);
                }
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
        String topics = System.getProperty(ENCRYPTION_TOPICS);
        if (StringUtils.hasText(topics)) {
            String[] topicsArray = topics.split(",");
            encryptionTopics.addAll(new HashSet<>(Arrays.asList(topicsArray)));
        }
    }
    private static class EncryptionException extends RuntimeException {
        public EncryptionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
