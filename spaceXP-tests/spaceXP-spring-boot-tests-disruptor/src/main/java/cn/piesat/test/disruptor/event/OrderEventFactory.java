package cn.piesat.test.disruptor.event;

import cn.piesat.test.disruptor.model.OrderEvent;
import com.lmax.disruptor.EventFactory;

/**
 * <p/>
 * {@code @description}: 初始化订单事件
 * <p/>
 * {@code @create}: 2024-10-16 14:01
 * {@code @author}: zhouxp
 */
public class OrderEventFactory implements EventFactory<OrderEvent> {
    @Override
    public OrderEvent newInstance() {
        return new OrderEvent();
    }
}
