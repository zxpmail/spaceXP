package cn.piesat.test.disruptor.config;

import cn.piesat.test.disruptor.event.OrderEventFactory;
import cn.piesat.test.disruptor.event.OrderEventHandler;
import cn.piesat.test.disruptor.model.OrderEvent;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p/>
 * {@code @description}: 配置类
 * <p/>
 * {@code @create}: 2024-10-16 14:05
 * {@code @author}: zhouxp
 */
@Configuration
public class DisruptorConfig {
    @Bean
    public RingBuffer<OrderEvent> getRingBuffer() {
        //定义用于事件处理的线程池， Disruptor通过java.util.concurrent.ExecutorService提供的线程来触发consumer的事件处理
        ExecutorService executor = Executors.newFixedThreadPool(2);
        //指定事件工厂
        OrderEventFactory factory = new OrderEventFactory();
        //指定ringbuffer字节大小，必须为2的N次方（能将求模运算转为位运算提高效率），否则将影响效率
        int bufferSize = 1024 * 256;

        //单线程模式，获取额外的性能
        //Disruptor<OrderEvent> disruptor = new Disruptor<>(factory, bufferSize, Executors.defaultThreadFactory());
        Disruptor<OrderEvent> disruptor = new Disruptor<>(factory, bufferSize, Executors.defaultThreadFactory(), ProducerType.SINGLE, new BlockingWaitStrategy());
        //设置事件业务处理器---消费者
        disruptor.handleEventsWith(new OrderEventHandler());
        // 启动disruptor线程
        disruptor.start();

        return disruptor.getRingBuffer();
    }
}
