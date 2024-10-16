package cn.piesat.test.disruptor.service.impl;

import cn.piesat.test.disruptor.model.OrderEvent;
import cn.piesat.test.disruptor.service.OrderProducerService;
import com.lmax.disruptor.RingBuffer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p/>
 * {@code @description}:
 * <p/>
 * {@code @create}: 2024-10-16 14:12
 * {@code @author}: zhouxp
 */
@Service
@Slf4j
public class OrderProducerServiceImpl implements OrderProducerService {
    private final RingBuffer<OrderEvent> ringBuffer;

    public OrderProducerServiceImpl(RingBuffer<OrderEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    @Override
    public void onData(String orderId, double amount) {
        //获取下一个Event槽的下标
        long sequence = ringBuffer.next();
        try {
            OrderEvent event = ringBuffer.get(sequence); // 获取该槽中的事件对象
            event.setOrderId(orderId);
            event.setAmount(amount);
            log.info("网消息队列中添加消息:{}",event);
        }catch (Exception e) {
            log.error("添加失败",e);
        } finally {
            //发布Event，激活观察者去消费，将sequence传递给消费者
            //注意最后的publish方法必须放在finally中以确保必须调用；如果某个请求的sequence未被提交将会堵塞后续的发布操作或者其他的producer
            ringBuffer.publish(sequence);  // 发布事件
        }
    }
}
