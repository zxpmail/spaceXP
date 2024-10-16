package cn.piesat.test.disruptor.event;

import cn.piesat.test.disruptor.model.OrderEvent;
import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * <p/>
 * {@code @description}: 处理事件
 * <p/>
 * {@code @create}: 2024-10-16 14:02
 * {@code @author}: zhouxp
 */
@Slf4j
public class OrderEventHandler implements EventHandler<OrderEvent> {
    /**
     * 订单处理逻辑，如保存数据库、调用支付接口等
     */
    @Override
    public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) {
        try {
            //这里停顿1000ms是为了确定消费消息是异步的
            Thread.sleep(1000);
            log.info("消费者处理消息开始");
            if (event != null) {
                log.info("消费者消费的消息是：{}  | Amount: {}",event.getOrderId(),event.getAmount());
            }
        } catch (Exception e) {
            log.info("消费者处理消息失败");
        }
        log.info("消费者处理消息结束");
    }
}
