package cn.piesat.framework.kafka.interceptor;

import cn.piesat.framework.common.utils.AesContextHolder;
import cn.piesat.framework.common.utils.AesUtils;
import cn.piesat.framework.kafka.utils.KafkaInitConfigureUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.springframework.util.AntPathMatcher;



import javax.crypto.Cipher;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.piesat.framework.kafka.constants.KafkaConstant.ENCRYPTION_TOPICS;


/**
 * <p/>
 * {@code @description}: 解密拦截器
 * <p/>
 * {@code @create}: 2025-01-14 10:16
 * {@code @author}: zhouxp
 */
@Slf4j
public class DecryptionConsumerInterceptor implements ConsumerInterceptor<String, String> {


    private final Set<String> encryptionTopics = new HashSet<>();

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public ConsumerRecords<String, String> onConsume(ConsumerRecords<String, String> records) {
        try {
            if (records == null) {
                throw new IllegalArgumentException("Input parameters cannot be null");
            }

            if (records.isEmpty() || encryptionTopics.isEmpty()) {
                return records;
            }

            Map<TopicPartition, List<ConsumerRecord<String, String>>> decryptedRecords = new LinkedHashMap<>();
            Map<String, Boolean> topicMatchCache = new HashMap<>();
            for (TopicPartition partition : records.partitions()) {
                List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);

                boolean shouldDecrypt = topicMatchCache.computeIfAbsent(partition.topic(), topic ->
                        encryptionTopics.stream()
                                .anyMatch(encTopic -> antPathMatcher.match(encTopic, topic)));

                if (shouldDecrypt) {
                    try{
                        List<ConsumerRecord<String, String>> decryptedPartitionRecords = partitionRecords.stream()
                                .map(record -> new ConsumerRecord<>(record.topic(), record.partition(),
                                        record.offset(), record.timestamp(), record.timestampType(),
                                        record.serializedKeySize(),
                                        record.serializedValueSize(), record.key(),
                                        AesUtils.decrypt(record.value()), new RecordHeaders(), Optional.empty()))
                                .collect(Collectors.toList());

                        decryptedRecords.put(partition, decryptedPartitionRecords);
                    }catch (Exception e){
                        log.debug("数据解密失败，可能数据有误",e);
                        decryptedRecords.put(partition, partitionRecords);
                    }
                } else {
                    decryptedRecords.put(partition, partitionRecords);
                }
            }

            return new ConsumerRecords<>(decryptedRecords);
        } catch (Exception e) {
            log.error("Error decrypting message", e);
            throw new RecordDecryptionException("Error decrypting message", e);
        }
    }

    @Override
    public void onCommit(Map<TopicPartition, OffsetAndMetadata> offsets) {

    }

    @Override
    public void close() {
        AesContextHolder.clear();
    }

    @Override
    public void configure(Map<String, ?> configs) {
        KafkaInitConfigureUtil.initConfigure(encryptionTopics, Cipher.DECRYPT_MODE);
    }
    private static class RecordDecryptionException extends RuntimeException {
        public RecordDecryptionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
