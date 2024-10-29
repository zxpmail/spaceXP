package cn.piesat.framework.kafka.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AlterConfigOp;
import org.apache.kafka.clients.admin.AlterConfigsOptions;
import org.apache.kafka.clients.admin.Config;
import org.apache.kafka.clients.admin.ConfigEntry;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.Nullable;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p/>
 * {@code @description}: kafka工具类
 * <p/>
 * {@code @create}: 2024-10-21 16:26
 * {@code @author}: zhouxp
 */
@Slf4j
public class KafkaUtil {
    private static AdminClient adminClient;

    private static KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    public static void setAdminClient(AdminClient adminClient) {
        KafkaUtil.adminClient = adminClient;
    }

    public static void setKafkaListenerEndpointRegistry(KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry) {
        KafkaUtil.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
    }

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

    /**
     *  创建kafka topic
     * @param topicName  topic名
     * @param partitions 分区数
     * @param replicas 副本数
     * @throws Exception
     */
    public static void createTopic(String topicName, int partitions, short replicas) throws Exception {
        NewTopic newTopic = new NewTopic(topicName, partitions, replicas);
        CreateTopicsResult topics = adminClient.createTopics(Collections.singleton(newTopic));
        topics.all().get();
        log.info("【{}】topic创建成功", topicName);
    }

    /**
     * 修改topic的过期时间
     * @param topicName topic名称
     * @param ms 过期时间（毫秒值）
     * @throws Exception
     */
    public static void updateTopicRetention(String topicName, String ms) throws Exception {
        Map<ConfigResource, Collection<AlterConfigOp>> configs =new HashMap<>();
        //声明一个ConfigResource，用来判断是给什么用的，此处是给topic，名字是java_test用的
        ConfigResource configResource=new ConfigResource(ConfigResource.Type.TOPIC,topicName);
        ConfigEntry configEntry = new ConfigEntry(TopicConfig.RETENTION_MS_CONFIG, ms);
        //设置一个AlterConfigOp对象用来，处理做什么操作，这里使用的SET(或者APPEND)类型的添加操作
        AlterConfigOp alterConfigOp=new AlterConfigOp(configEntry,AlterConfigOp.OpType.SET);
        //添加到Collection中，用于传到incrementalAlterConfigs方法中
        Collection<AlterConfigOp> configOps=new ArrayList<>();
        configOps.add(alterConfigOp);
        //给map赋值
        configs.put(configResource,configOps);
        // 创建AlterConfigsOptions
        AlterConfigsOptions alterConfigsOptions = new AlterConfigsOptions().timeoutMs(10000);
        // 执行修改操作
        adminClient.incrementalAlterConfigs(configs,alterConfigsOptions).all().get();
        log.info("【{}】topic过期时间设置完成，过期时间为：{}毫秒", topicName, ms);
    }

    /**
     * 列出 topic
     * @return topic列表
     * @throws Exception
     */
    public static Set<String> listTopic() throws Exception {
        ListTopicsResult listTopicsResult = adminClient.listTopics();
        return listTopicsResult.names().get();
    }

    /**
     * 判断topic是否存在
     * @param topicName topic名
     */
    public static boolean existTopic(String topicName) throws Exception {
        Set<String> strings = listTopic();
        if (strings.isEmpty()) {
            return false;
        }
        return strings.contains(topicName);
    }
}
