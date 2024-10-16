package cn.piesat.test.disruptor.model;

import lombok.Data;

/**
 * <p/>
 * {@code @description}: 订单实体类
 * <p/>
 * {@code @create}: 2024-10-16 13:58
 * {@code @author}: zhouxp
 */
@Data
public class OrderEvent {
    private String orderId;
    private double amount;
}
