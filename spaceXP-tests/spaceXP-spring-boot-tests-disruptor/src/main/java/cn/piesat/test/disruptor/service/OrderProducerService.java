package cn.piesat.test.disruptor.service;

/**
 * <p/>
 * {@code @description}:
 * <p/>
 * {@code @create}: 2024-10-16 14:11
 * {@code @author}: zhouxp
 */
public interface OrderProducerService {
    void onData(String orderId, double amount);
}
