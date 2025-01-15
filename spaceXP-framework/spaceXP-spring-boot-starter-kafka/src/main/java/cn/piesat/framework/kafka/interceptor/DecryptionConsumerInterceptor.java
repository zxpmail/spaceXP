package cn.piesat.framework.kafka.interceptor;

import cn.piesat.framework.common.utils.AesUtils;
import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p/>
 * {@code @description}: 解密拦截器
 * <p/>
 * {@code @create}: 2025-01-14 10:16
 * {@code @author}: zhouxp
 */
public class DecryptionConsumerInterceptor implements ConsumerInterceptor<String, String> {
    private static final String IGNORE_TOPICS = "ignoreTopics";

    private String[] ignoreTopics;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Override

    public ConsumerRecords<String, String> onConsume(ConsumerRecords<String, String> records) {
        try {
            // 处理空记录情况
            if (records.isEmpty()) {
                return records;
            }
            // 忽略主题的检查
            if (ignoreTopics != null && ignoreTopics.length > 0) {
                List<TopicPartition> ignoredPartitions = records.partitions().stream()
                        .filter(partition -> Arrays.stream(ignoreTopics)
                                .anyMatch(topic -> antPathMatcher.match(topic, partition.topic())))
                        .collect(Collectors.toList());

                if (!ignoredPartitions.isEmpty()) {
                    return records;
                }
            }
            Map<TopicPartition, List<ConsumerRecord<String, String>>> decryptedRecords = new LinkedHashMap<>();
            for (TopicPartition partition : records.partitions()) {
                // 获取该分区的所有记录
                List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);

                // 对每个记录进行解密，并添加到对应分区的列表中
                List<ConsumerRecord<String, String>> decryptedPartitionRecords = new ArrayList<>(partitionRecords.size());
                for (ConsumerRecord<String, String> record : partitionRecords) {
                    String decryptedValue = AesUtils.decrypt(record.value());
                    // 将解密后的记录添加到列表中
                    decryptedPartitionRecords.add(new ConsumerRecord<>(record.topic(), record.partition(),
                            record.offset(), record.timestamp(), record.timestampType(),
                            record.serializedKeySize(),
                            record.serializedValueSize(), record.key(), decryptedValue, new RecordHeaders(), Optional.empty()));
                }
                decryptedRecords.put(partition, decryptedPartitionRecords);
            }
            // 返回新的ConsumerRecords实例
            return new ConsumerRecords<>(decryptedRecords);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting message", e);
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
        String topics = System.getProperty(IGNORE_TOPICS);
        if (StringUtils.hasText(topics)) {
            ignoreTopics = topics.split(",");
        }
    }
}
