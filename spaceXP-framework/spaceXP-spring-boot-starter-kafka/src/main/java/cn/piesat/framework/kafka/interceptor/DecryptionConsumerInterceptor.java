package cn.piesat.framework.kafka.interceptor;

import cn.piesat.framework.common.utils.AesUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;


import java.util.Arrays;
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
            for (TopicPartition partition : records.partitions()) {
                List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);

                boolean shouldDecrypt = encryptionTopics.stream()
                        .anyMatch(topic -> antPathMatcher.match(topic, partition.topic()));

                if (shouldDecrypt) {
                    List<ConsumerRecord<String, String>> decryptedPartitionRecords = partitionRecords.stream()
                            .map(record -> new ConsumerRecord<>(record.topic(), record.partition(),
                                    record.offset(), record.timestamp(), record.timestampType(),
                                    record.serializedKeySize(),
                                    record.serializedValueSize(), record.key(),
                                    AesUtils.decrypt(record.value()), new RecordHeaders(), Optional.empty()))
                            .collect(Collectors.toList());

                    decryptedRecords.put(partition, decryptedPartitionRecords);
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

    }

    @Override
    public void configure(Map<String, ?> configs) {
        String topics = System.getProperty(ENCRYPTION_TOPICS);
        if (StringUtils.hasText(topics)) {
            String[] topicsArray = topics.split(",");
            encryptionTopics.addAll(new HashSet<>(Arrays.asList(topicsArray)));
        }
    }
    private static class RecordDecryptionException extends RuntimeException {
        public RecordDecryptionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
