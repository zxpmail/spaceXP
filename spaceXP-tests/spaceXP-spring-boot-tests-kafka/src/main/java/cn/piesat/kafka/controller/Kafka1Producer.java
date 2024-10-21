package cn.piesat.kafka.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p/>
 * {@code @description}: kafka 生产者 · 发送消息
 * <p/>
 * {@code @create}: 2024-10-21 10:25
 * {@code @author}: zhouxp
 */

@RestController
public class Kafka1Producer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    /**
     * 配置属性读取配置，
     * 然后将 {@link KafkaTemplate} 模板添加到 Spring 容器中，所以这里直接获取使用即可。
     */
    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * 向指定主题(topic)发送消息：http://localhost:8080/kafka/sendMsg?topic=car-infos
     * <p>
     * 1、send(String topic, @Nullable V data)：向指定主题发送消息，如果 topic 不存在，则自动创建，
     * 但是创建的主题默认只有一个分区 - PartitionCount: 1、分区也没有副本 - ReplicationFactor: 1，1表示自身。
     * 2、send 方法默认是异步的，主线程会直接继续向后运行，想要获取发送结果是否成功，请添加回调方法 addCallback。
     * [WARN ][org.apache.kafka.common.utils.LogContext$KafkaLogger.warn(LogContext.java:241)]:[Producer clientId=producer-1] Connection to node -1 could not be established. Broker may not be available.
     * [ERROR][org.springframework.kafka.support.LoggingProducerListener.onError(LoggingProducerListener.java:76)]:Exception thrown when sending a message with key='xxx' and payload='xxx' to topic bgt.basic.agency.frame.topic:
     * 3、send().get() 可以同步阻塞主线程直到获取执行结果，或者执行超时抛出异常.
     * java.util.concurrent.ExecutionException: org.springframework.kafka.core.KafkaProducerException:
     * Failed to send; nested exception is org.apache.kafka.common.errors.TimeoutException: Failed to update metadata after 60000 ms.
     *
     * @param topic   ：主题名称，不存在时自动创建，默认1个分区，无副本。主题名称也可以通过配置文件配置，这里直接通过参数传入。
     * @param message ：待发送的消息，如：{"version":1,"text":"后日凌晨三点执行任务"}
     * @return
     */
    @PostMapping("kafka/sendMsg")
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> sendMessage(@RequestParam String topic, @RequestBody Map<String, Object> message) {
        logger.info("向指定主题发送信息，topic={},message={}", topic, message);
        try {
            String valueAsString = new ObjectMapper().writeValueAsString(message);
            // 异步
            // kafkaTemplate.send(topic, valueAsString);
            // 同步：get() 获取执行结果，此时线程将阻塞，等待执行结果
            SendResult<String, Object> sendResult = kafkaTemplate.send(topic, valueAsString).get();
            sendResult.toString();
            message.put("sendResult", sendResult.toString());
            // org.apache.kafka.common.errors.TimeoutException: Failed to update metadata after 60000 ms.
        } catch (Exception e) {
            // 异步发送时子线程中的异常是不会进入这里的，只有同步发送时，主线程阻塞，发送是吧，抛出异常时，才会进入这里。
            e.printStackTrace();
        }
        return message;
    }

    /**
     * 向默认主题(default-topic)发送消息：http://localhost:8080/kafka/sendMsgDefault
     * 默认主题由 spring.kafka.template.default-topic 选项进行配置
     *
     * @param message ：待发送的消息，如：{"version":2,"text":"后日凌晨三点执行任务，不得有误"}
     * @return
     */
    @PostMapping("kafka/sendMsgDefault")
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> sendMsgDefault(@RequestBody Map<String, Object> message) {
        logger.info("向默认主题发送信息，topic={},topic={}", kafkaTemplate.getDefaultTopic(), message);
        try {
            String valueAsString = new ObjectMapper().writeValueAsString(message);
            kafkaTemplate.sendDefault(valueAsString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * 异步回调写法 1
     * 发送信息，并添加异步回调方法，用于监控消息发送成功或者失败。发送成功可以记录日志，发送失败则应该有相应的措施，比如延期再发送等。
     * http://localhost:8080/kafka/sendMsgCallback?topic=car-infos
     * 1、addCallback 方法用于获取 send 发送的结果，成功或者失败，此时 send 方法不再阻塞线程。
     *
     * @param topic   ：car-infos
     * @param message ：{"version":223,"text":"后日凌晨三点执行任务，不得有误"}
     * @return
     */
    @PostMapping("kafka/sendMsgCallback")
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> sendMessageCallback(@RequestParam String topic,
                                                   @RequestBody Map<String, Object> message) {
        try {
            String valueAsString = new ObjectMapper().writeValueAsString(message);
            /**
             * addCallback：添加成功或者失败的异步回调
             * {@link SuccessCallback}：是发送成功回调，函数式接口，其中的方法参数为 {@link SendResult}，表示发送结果
             * {@link FailureCallback}：是发送失败回调，函数式接口，其中的方法参数为 Throwable，表示异常对象
             */
            kafkaTemplate.send(topic, valueAsString).addCallback(success -> {
                String topic2 = success.getRecordMetadata().topic();
                int partition = success.getRecordMetadata().partition();
                long offset = success.getRecordMetadata().offset();
                logger.info("发送消息成功，topic={},partition={},offset={}", topic2, partition, offset);
            }, failure -> {
                logger.warn("消息发送失败：{}，{}", failure.getMessage(), failure);
                logger.warn("保存到数据库中，后期再做处理.");
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        logger.info("向指定主题发送信息，回调，topic={},message={}", topic, message);
        return message;
    }

    /**
     * 异步回调写法 2
     * 发送信息，并添加异步回调方法，用于监控消息发送成功或者失败。发送成功可以记录日志，发送失败则应该有相应的措施，比如延期再发送等。
     * http://localhost:8080/kafka/sendMsgCallback2?topic=helloWorld
     * 1、addCallback 方法用于获取 send 发送的结果，成功或者失败，此时 send 方法不再阻塞线程，主线程会直接运行过去。
     *
     * @param topic   ：helloWorld
     * @param message ：{"version":223,"text":"后日凌晨三点执行任务，不得有误"}
     * @return
     */
    @PostMapping("kafka/sendMsgCallback2")
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> sendMessageCallback2(@RequestParam String topic,
                                                    @RequestBody Map<String, Object> message) {
        try {
            String valueAsString = new ObjectMapper().writeValueAsString(message);
            /**
             * ListenableFutureCallback 接口继承了 {@link SuccessCallback}、 {@link FailureCallback} 函数式接口
             * 重写方法即可
             */
            kafkaTemplate.send(topic, valueAsString).addCallback(
                    new ListenableFutureCallback<SendResult<String, Object>>() {
                        @Override
                        public void onSuccess(SendResult<String, Object> success) {
                            int partition = success.getRecordMetadata().partition();
                            long offset = success.getRecordMetadata().offset();
                            String topic2 = success.getRecordMetadata().topic();
                            logger.info("发送消息成功，topic={},partition={},offset={}", topic2, partition, offset);
                        }

                        @Override
                        public void onFailure(Throwable failure) {
                            logger.warn("消息发送失败：{}，{}", failure.getMessage(), failure);
                            logger.warn("保存到数据库中，后期再做处理.");
                        }
                    });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        logger.info("向指定主题发送信息，回调，topic={},message={}", topic, message);
        return message;
    }

    /**
     * 向指定主题(topic)发送消息：http://localhost:8080/kafka/sendMsgTransactional1?topic=car-infos
     * 与 springframework 框架的事务整合到一起，此时异常处理完全和平时一样.
     *
     * @param topic   ：主题名称，不存在时自动创建，默认1个分区，无副本。主题名称也可以通过配置文件配置，这里直接通过参数传入。
     * @param message ：待发送的消息，如：{"version":1,"text":"后日凌晨三点执行任务"}
     * @return
     */
    @PostMapping("kafka/sendMsgTransactional1")
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> sendMessageTransactional1(@RequestParam String topic,
                                                         @RequestBody Map<String, Object> message) {
        try {
            logger.info("向指定主题发送信息，带事务管理，topic={},message={}", topic, message);
            String msg = new ObjectMapper().writeValueAsString(message);
            ListenableFuture<SendResult<String, Object>> send = kafkaTemplate.send(topic, msg);
            if ("110".equals(message.get("version").toString())) {
                TimeUnit.SECONDS.sleep(3);
                System.out.println(1 / 0);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * http://localhost:8080/kafka/sendMsgTransactional2?topic=car-infos
     * 生成者发送消息事务管理方式2：使用 executeInTransaction(OperationsCallback<K, V, T> callback)
     * executeInTransaction：表示执行本地事务，不参与全局事务(如果存在)，即方法内部和外部是分离的，只要内部不
     * 发生异常，消息就会发送，与外部无关，即使外部有 @Transactional 注解也不影响消息发送，此时外围有没有 @Transactional 都一样。
     *
     * @param topic
     * @param message
     * @return
     */
    @PostMapping("kafka/sendMsgTransactional2")
    public Map<String, Object> sendMessageTransactional2(@RequestParam String topic,
                                                         @RequestBody Map<String, Object> message) {
        try {
            logger.info("向指定主题发送信息，带事务管理：topic={},message={}", topic, message);
            String msg = new ObjectMapper().writeValueAsString(message);

            /**
             * executeInTransaction 表示这些操作在本地事务中调用，不参与全局事务（如果存在）
             * 所以回调方法内部发生异常时，消息不会发生出去，但是方法外部发生异常不会回滚，即便是外围方法加了 @Transactional 也没用。
             */
            kafkaTemplate.executeInTransaction(operations -> {
                operations.send(topic, msg);
                if ("120".equals(message.get("version").toString())) {
                    System.out.println(1 / 0);
                }
                return null;
            });
            //如果在这里发生异常，则只要 executeInTransaction 里面不发生异常，它仍旧会发生消息成功
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return message;
    }

}
