package cn.piesat.kafka.utils;

import cn.piesat.framework.common.utils.SpringBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AlterConfigsOptions;
import org.apache.kafka.clients.admin.Config;
import org.apache.kafka.clients.admin.ConfigEntry;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.config.MethodKafkaListenerEndpoint;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

/**
 * <p/>
 * {@code @description}: 用于创建kafka Topic队列和listener监听容器的工具类
 * <p/>
 * {@code @create}: 2024-10-18 15:20
 * {@code @author}: zhouxp
 */

//@Component
@Slf4j
public class KafkaUtil {

    private static AdminClient adminClient;

    private static KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    private static KafkaTemplate kafkaTemplate;

    /**
     * @Title KafkaUtil
     * @Description 构造函数注入
     * @param adminClient kafka客户端对象
     * @param kafkaListenerEndpointRegistry kafka监听容器注册对象
     * @param kafkaListenerEndpointRegistry kafka生产者工具类
     * @return
     */
    @Autowired
    public KafkaUtil(AdminClient adminClient, KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry, KafkaTemplate kafkaTemplate) {
        KafkaUtil.adminClient = adminClient;
        KafkaUtil.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
        KafkaUtil.kafkaTemplate = kafkaTemplate;
    }

    //region topic相关方法


    /**
     * @Title createTopic
     * @Description 创建kafka topic
     * @param topicName topic名
     * @param partitions 分区数
     * @param replicas 副本数（short）
     * @return void
     */
    public static void createTopic(String topicName, int partitions, short replicas) throws Exception {
        NewTopic newTopic = new NewTopic(topicName, partitions, replicas);
        CreateTopicsResult topics = adminClient.createTopics(Collections.singleton(newTopic));
        topics.all().get();
        log.info("【{}】topic创建成功", topicName);
    }

    /**
     * @Title deleteTopic
     * @Description 删除topic
     * @param topicName  topic名称
     * @return void
     */
    public static void deleteTopic(String topicName) throws Exception {
        DeleteTopicsResult deleteTopicsResult = adminClient.deleteTopics(Collections.singleton(topicName));
        deleteTopicsResult.all().get();
        log.info("【{}】topic删除成功", topicName);

    }

    /**
     * @Title updateTopicRetention
     * @Description 修改topic的过期时间
     * @param topicName  topic名称
     * @param ms  过期时间（毫秒值）
     * @return void
     */
    public static void updateTopicRetention(String topicName, String ms) throws Exception {
        ConfigResource resource = new ConfigResource(ConfigResource.Type.TOPIC, topicName);
        ConfigEntry configEntry = new ConfigEntry(TopicConfig.RETENTION_MS_CONFIG, ms);
        Config config = new Config(Collections.singleton(configEntry));
        // 创建AlterConfigsOptions
        AlterConfigsOptions alterConfigsOptions = new AlterConfigsOptions().timeoutMs(10000);
        // 执行修改操作
        adminClient.alterConfigs(Collections.singletonMap(resource, config), alterConfigsOptions).all().get();
        log.info("【{}】topic过期时间设置完成，过期时间为：{}毫秒", topicName, ms);
    }


    /**
     * @Title listTopic
     * @Description 获取topic列表
     * @return java.util.Set<java.lang.String>
     */
    public static Set<String> listTopic() throws Exception {
        ListTopicsResult listTopicsResult = adminClient.listTopics();
        Set<String> strings = listTopicsResult.names().get();
        return strings;
    }


    /**
     * @Title existTopic
     * @Description topic是否存在
     * @param topicName topic名称
     * @return boolean
     */
    public static boolean existTopic(String topicName) throws Exception {
        Set<String> strings = listTopic();
        if (strings == null || strings.isEmpty()) {
            return false;
        }
        return strings.contains(topicName);
    }


    //endregion

    //region 生产者发送消息示例

    /**
     * @Title sendMsg
     * @Description 通过注册信息找到对应的容器并启动
     * @param topic 队列名称
     * @param msg 消息
     * @return void
     */
    public static void sendMsg(String topic, Object msg) throws Exception {
        kafkaTemplate.send(topic, msg);
        //kafkaTemplate.send(topic,2,"key",msg);//带有分区和key值的
    }
    //endregion

    //region 消费者监听容器相关方法


    /**
     * @Title existListenerContainer
     * @Description TODO 根据ID查询容器是否存在
     * @param id 监听容器id
     * @return boolean
     */
    public static boolean existListenerContainer(String id) throws Exception {
        Set<String> listenerIds = kafkaListenerEndpointRegistry.getListenerContainerIds();
        return listenerIds.contains(id);
    }


    /**
     * @Title registerListener
     * @Description TODO  创建kafka监听容器并注册到注册信息中，一次可以注册多个topic的监听容器
     * @param id 容器id，自定义
     * @param consumerGroupId 消费者组id自定义
     * @param processBean 处理消息的类
     * @param processMethod 处理消息的方法
     * @param topics 需要监听的topic数组
     * @return void
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


//        for (String topicName : topics) {
//            if (!KafkaConfig.notExistTopicCreateContainerFlag && !nameTopics.contains(topicName)) {
//                log.info("【{}】topic不存在，不创建容器！", topicName);
//                continue;
//            }
//            //创建一个kafka监听器端点对象
//            MethodKafkaListenerEndpoint<String, String> endpoint = new MethodKafkaListenerEndpoint<>();
//            //设置监听器端点相关信息
//            //设置Id
//            endpoint.setId(topicName);
//            //设置消费者组
//            endpoint.setGroupId(topicName + "_consumer_group");
//            //设置主题
//            endpoint.setTopics(topicName);
//            //设置每个监听器线程数
//            endpoint.setConcurrency(3);
//            //设置批量监听
//            endpoint.setBatchListener(true);
//            //设置默认处理工厂
//            endpoint.setMessageHandlerMethodFactory(new DefaultMessageHandlerMethodFactory());
//            //设置实际处理的Bean对象
//            endpoint.setBean(new ConsumerController());
//            //设置实际处理的方法名和参数类型
//            endpoint.setMethod(ConsumerController.class.getMethod("consumeMessage", String.class));
//            //注册Container并启动
//            kafkaListenerEndpointRegistry.registerListenerContainer(endpoint, SpringUtil.getBean(KafkaListenerContainerFactory.class), true);
//            log.info("Kafka监听容器操作：ID为{}的容器已【注册】", topicName);
//        }
    }


    /**
     * @Title startListenerContainer
     * @Description 根据id开启监听容器的运行状态
     * @param id 监听容器的id
     * @return void
     */
    public static void startListenerContainer(String id) throws Exception {
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer(id);
        if (listenerContainer == null) {
            log.info("Kafka监听容器操作：ID为{}的容器不存在，不操作！", id);
            return;
        }
        listenerContainer.start();
        log.info("Kafka监听容器操作：ID为{}的容器已【开启】", id);
    }


    /**
     * @Title stopListenerContainer
     * @Description TODO 根据id停止监听容器的运行状态
     * @param id 监听容器的id
     * @return void
     */
    public static void stopListenerContainer(String id) throws Exception {
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer(id);
        if (listenerContainer == null) {
            log.info("Kafka监听容器操作：ID为{}的容器不存在，不操作！", id);
            return;
        }
        listenerContainer.stop();
        log.info("Kafka监听容器操作：ID为{}的容器已【停止】", id);
    }


    /**
     * @Title pauseListenerContainer
     * @Description TODO 根据id暂停监听容器的监听状态
     * @param id 监听容器的id
     * @return void
     */
    public static void pauseListenerContainer(String id) throws Exception {
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer(id);
        if (listenerContainer == null) {
            log.info("Kafka监听容器操作：ID为{}的容器不存在，不操作！", id);
            return;
        }
        listenerContainer.pause();
        log.info("Kafka监听容器操作：ID为{}的容器已【暂停】", id);
    }

    /**
     * @Title resumeListenerContainer
     * @Description TODO  根据id恢复监听容器的监听状态
     * @param id 监听容器的id
     * @return void
     */
    public static void resumeListenerContainer(String id) throws Exception {
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer(id);
        if (listenerContainer == null) {
            log.info("Kafka监听容器操作：ID为{}的容器不存在，不操作！", id);
            return;
        }
        listenerContainer.resume();
        log.info("Kafka监听容器操作：ID为{}的容器已【恢复】", id);
    }


    /**
     * @Title isNormalStateListenerContainer
     * @Description 是否是正常状态的容器
     * （kafka监听容器的运行状态标志是running，监听状态标志是pauseRequested，停止是关闭了资源，暂停是停止消费）
     *  只有running是true，并且pauseRequested是false，监听容器才能正常消费消息
     * @param id 监听容器的id
     * @return boolean
     */
    public static boolean isNormalStateListenerContainer(String id) throws Exception {
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer(id);
        //如果不存在此id容器，则返回false
        if (listenerContainer == null) {
            return false;
        }
        //存在则返回容器的运行状态和非暂停状态
        return listenerContainer.isRunning() && !listenerContainer.isPauseRequested();
    }


    /**
     * @Title getPauseStateListenerContainer
     * @Description 获取监听容器的暂停状态（监听的状态）
     * @param id 监听容器id
     * @return boolean
     */
    public static boolean getPauseStateListenerContainer(String id) throws Exception {
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer(id);
        if (listenerContainer == null) {
            return true;
        }
        return listenerContainer.isPauseRequested();

    }

    /**
     * @Title getRunningStateListenerContainer
     * @Description 获取监听容器的运行状态（容器的状态）
     * @param id 监听容器id
     * @return boolean
     */
    public static boolean getRunningStateListenerContainer(String id) throws Exception {
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer(id);
        if (listenerContainer == null) {
            return false;
        }
        return listenerContainer.isRunning();
    }

    /**
     * @Title setStateNormalListenerContainer
     * @Description 使容器的运行状态和监听状态都是正常
     * @param id 监听容器的id
     * @return boolean 正常返回true，非正常返回false
     */
    public static boolean setStateNormalListenerContainer(String id) throws Exception {
        if (!existListenerContainer(id)) {
            log.info("Kafka监听容器操作：ID为{}的容器不存在，不操作！", id);
            return false;
        }
        //先判断容器运行状态是否正常，如果不正常，则开启
        if (!getRunningStateListenerContainer(id)) {
            startListenerContainer(id);
        }
        //再判断容器监听状态是否正常，如果不正常，则恢复
        if (getPauseStateListenerContainer(id)) {
            resumeListenerContainer(id);
        }
        //设置完后，再查询状态并返回。
        return isNormalStateListenerContainer(id);
    }

}
