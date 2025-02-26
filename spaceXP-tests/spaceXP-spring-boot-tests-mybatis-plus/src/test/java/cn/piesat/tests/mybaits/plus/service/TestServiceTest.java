package cn.piesat.tests.mybaits.plus.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.concurrent.CountDownLatch;


/**
 * <p/>
 * {@code @description}: 模板配置测试类
 * <p/>
 * {@code @create}: 2024-07-16 14:55
 * {@code @author}: zhouxp
 */
@SpringBootTest
public class TestServiceTest {

    @Autowired
    private TestService testService;

    @Test
    public void testCustomAnnotation() {
        testService.insertUsersBySql();
        testService.insertUsersWithBatchProcessing();
        testService.insertUsersWithJdbcBatch();
        testService.insertUsersWithCustomBatch();
        testService.insertUsersOneByOne();
    }


    public static void main(String[] args) {
        int threadCount = 30;
        RestTemplate restTemplate = new RestTemplate();
        CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {// 循环开30个线程
            new Thread(new Runnable() {
                public void run() {
                    COUNT_DOWN_LATCH.countDown();// 每次减一
                    try {
                        COUNT_DOWN_LATCH.await(); // 此处等待状态，为了让30个线程同时进行
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    for (int j = 1; j <= 3; j++) {
                        int param = new Random().nextInt(4);
                        if (param <= 0) {
                            param++;
                        }
                        String responseBody = restTemplate.getForObject("http://localhost:8080//user/merge?userId=" + param, String.class);
                        System.out.println(Thread.currentThread().getName() + "参数 " + param + " 返回值 " + responseBody);
                    }
                }
            }).start();
        }
    }
}
