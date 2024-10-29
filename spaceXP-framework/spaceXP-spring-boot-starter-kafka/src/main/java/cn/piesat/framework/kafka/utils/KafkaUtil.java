package cn.piesat.framework.kafka.utils;

import cn.piesat.framework.common.utils.SpringBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AlterConfigOp;
import org.apache.kafka.clients.admin.AlterConfigsOptions;
import org.apache.kafka.clients.admin.ConfigEntry;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.config.MethodKafkaListenerEndpoint;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.Nullable;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.lang.reflect.Method;
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

    /**
     * 根据ID查询容器是否存在
     * @param id 监听容器id
     */
    public static boolean existListenerContainer(String id)  {
        Set<String> listenerIds = kafkaListenerEndpointRegistry.getListenerContainerIds();
        return listenerIds.contains(id);
    }

    /**
     * 创建kafka监听容器并注册到注册信息中，一次可以注册多个topic的监听容器
     * @param id 容器id，自定义
     * @param consumerGroupId  消费者组id自定义
     * @param processBean 处理消息的类
     * @param processMethod 处理消息的方法
     * @param topics 需要监听的topic数组
     */
    public static void registerListenerContainer(String id, String consumerGroupId, Object processBean, Method processMethod, String... topics) throws Exception {
        //判断id是否存在
        if (existListenerContainer(id)) {
            //如果当前id的容器已存在，不添加
            log.info("当前id为{}的容器已存在，不进行添加操作！", id);
            return;
        }
        //判断所有队列是否存在
        for (String topic : topics) {
            if (!existTopic(topic)) {
                //如果存在topic不存在，不添加
                log.info("【{}】topic不存在，不进行添加操作！", topic);
                return;
            }
        }
        MethodKafkaListenerEndpoint<String, String> endpoint = new MethodKafkaListenerEndpoint<>();
        //设置监听器端点相关信息
        //设置Id
        endpoint.setId(id);
        //设置消费者组
        endpoint.setGroupId(consumerGroupId);
        //设置要监听的topic数组，可以是多个
        endpoint.setTopics(topics);
        //设置每个监听器线程数
        endpoint.setConcurrency(3);
        //设置批量监听
        endpoint.setBatchListener(true);
        //设置消息处理工厂类，这里用的是默认工厂
        endpoint.setMessageHandlerMethodFactory(new DefaultMessageHandlerMethodFactory());
        //设置实际处理的Bean对象，即实际的对象，比如new Class();
        endpoint.setBean(processBean);
        //设置实际处理的方法(包含方法名和参数)
        endpoint.setMethod(processMethod);
        //注册Container并启动，startImmediately表示立马启动
        kafkaListenerEndpointRegistry.registerListenerContainer(endpoint, SpringBeanUtil.getBean(KafkaListenerContainerFactory.class), true);
        log.info("Kafka监听容器操作：ID为{}的容器已【注册】，监听的topics：{}", id, topics);
        
    }
}
