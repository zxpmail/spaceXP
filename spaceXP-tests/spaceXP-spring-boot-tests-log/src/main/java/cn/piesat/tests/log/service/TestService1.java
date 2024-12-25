package cn.piesat.tests.log.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * <p/>
 * {@code @description}  :测试日志
 * <p/>
 * <b>@create:</b> 2022/12/9 17:25.
 *
 * @author zhouxp
 */
@Service
@Slf4j
public class TestService1 {
    @Async("mdcExecutor")
    public void testMdc() {
        log.info("test mdc ........");

        throw new RuntimeException("hello");
    }

    @Async("mdcExecutor")
    public String  hello() {
        log.info("test hello ........");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return  "hello";
    }

    public String  hello1() {
        log.info("test hello1 ........");
        return  "hello1";
    }
}
