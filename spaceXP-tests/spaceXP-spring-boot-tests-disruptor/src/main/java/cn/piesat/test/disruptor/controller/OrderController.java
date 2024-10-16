package cn.piesat.test.disruptor.controller;

import cn.piesat.test.disruptor.service.OrderProducerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p/>
 * {@code @description}: 发送订单事件
 * <p/>
 * {@code @create}: 2024-10-16 14:08
 * {@code @author}: zhouxp
 */
@RestController
public class OrderController {

    private final OrderProducerService producerService;

    public OrderController(OrderProducerService producerService) {
        this.producerService = producerService;
    }


    @PostMapping("/order")
    public String createOrder(@RequestParam String orderId, @RequestParam double amount) {
        producerService.onData(orderId, amount);
        return "Order submitted successfully.";
    }
}