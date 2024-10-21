package cn.piesat.framework.kafka.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.Nullable;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * <p/>
 * {@code @description}: kafka工具类
 * <p/>
 * {@code @create}: 2024-10-21 16:26
 * {@code @author}: zhouxp
 */
@Slf4j
public class KafkaUtil {
    public static <K, V> void sendMessage(KafkaTemplate<K, V> kafkaTemplate, String topic, V value) {
        ListenableFuture<SendResult<K, V>> future = kafkaTemplate.send(topic, value);
        future.addCallback(new ListenableFutureCallback<SendResult<K, V>>() {
            @Override
            public void onSuccess(@Nullable SendResult<K, V> result) {
                if (result == null || result.getRecordMetadata() == null) {
                    log.error("发送数据成功，topic:{},result is null ", topic);
                    return;
                }
                RecordMetadata recordMetadata = result.getRecordMetadata();
                String topic = recordMetadata.topic();
                int partition = recordMetadata.partition();
                long offset = recordMetadata.offset();
                log.info("发送数据成功，topic:{},partition:{},offset:{}", topic, partition, offset);
            }

            @Override
            public void onFailure(Throwable e) {
                log.error("发送数据失败：{}", e.getMessage(), e);
            }
        });
    }
}
