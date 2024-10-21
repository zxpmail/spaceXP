package cn.piesat.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * <p/>
 * {@code @description}: 消费者定时器——定时开关消费者消费功能
 * 1、本类使用 @EnableScheduling 定时任务的方式开关消费者监听器，同理可以自己提供控制层接口，通过 http 的方式来开关。
 * <p/>
 * {@code @create}: 2024-10-21 10:19
 * {@code @author}: zhouxp
 */
@Component
@EnableScheduling
@EnableAsync
public class ConsumerTimer {

    /**
     * 1、{@link KafkaListener} 注解标注的方法会被注册在 KafkaListenerEndpointRegistry 中。
     * 2、{@link KafkaListenerEndpointRegistry} 在 Spring IOC 容器中已经存在，可以直接取。
     */
    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    /**
     * 定时启动消费者监听器
     * <p>
     * MessageListenerContainer getListenerContainer(String id)
     * * 1、返回具有指定id的{@link MessageListenerContainer}，如果不存在此类容器，则返回 null。
     * * 2、这个 id 就是 @KafkaListener 注解的 id 属性值
     * Set<String> getListenerContainerIds()：获取所有的 KafkaListener 监听器 id
     * Collection<MessageListenerContainer> getListenerContainers()：获取所有的 KafkaListener 监听器容器
     */
    @Scheduled(cron = "0 52 20 * * ? ")
    public void startListener() {
        Set<String> containerIds = kafkaListenerEndpointRegistry.getListenerContainerIds();
        containerIds.stream().forEach(item -> System.out.println("KafkaListener 消费者监听器：" + item));

        //boolean isRunning()：检查此组件当前是否正在运行
        //void start()：启动此组件,如果组件已在运行，则不应引发异常，配合 stop 方法使用，
        //void resume()：如果暂停，在下一次轮询后恢复此容器，配合 pause 方法使用。
        kafkaListenerEndpointRegistry.getListenerContainer("basicConsumer").resume();
        //kafkaListenerEndpointRegistry.getListenerContainer("basicConsumer").start();
        System.out.println(LocalDateTime.now() + " 启动 kafka 消费者监听器：basicConsumer");
    }

    /**
     * 定时关闭/暂停消费者监听器
     * void pause()：在下次轮询之前暂停此容器，配合 resume
     * void stop()：以同步方式停止此组件/容器，如果组件未运行(尚未启动)，则不应引发异常。配合 start 方法重新启动
     */
    @Scheduled(cron = "0 50 20 * * ? ")
    public void shutDownListener() {
        kafkaListenerEndpointRegistry.getListenerContainer("basicConsumer").pause();
        //kafkaListenerEndpointRegistry.getListenerContainer("basicConsumer").stop();
        System.out.println(LocalDateTime.now() + " 暂停 kafka 消费者监听器：basicConsumer");
    }


}
