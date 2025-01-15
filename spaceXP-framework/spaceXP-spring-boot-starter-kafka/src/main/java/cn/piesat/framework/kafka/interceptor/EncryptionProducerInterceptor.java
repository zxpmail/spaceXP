package cn.piesat.framework.kafka.interceptor;

import cn.piesat.framework.common.utils.AesUtils;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * <p/>
 * {@code @description}: 加密拦截器
 * <p/>
 * {@code @create}: 2025-01-14 10:05
 * {@code @author}: zhouxp
 */
public class EncryptionProducerInterceptor implements ProducerInterceptor<String, String> {
    private static final String IGNORE_TOPICS = "ignoreTopics";

    private String[] ignoreTopics;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        if (ignoreTopics != null && ignoreTopics.length > 0) {
            for (String ignoreTopic : ignoreTopics) {
                if (antPathMatcher.match(ignoreTopic, record.topic())) {
                    return record;
                }
            }
        }
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
        String topics = System.getProperty(IGNORE_TOPICS);
        if (StringUtils.hasText(topics)) {
            ignoreTopics = topics.split(",");
        }
    }
}
