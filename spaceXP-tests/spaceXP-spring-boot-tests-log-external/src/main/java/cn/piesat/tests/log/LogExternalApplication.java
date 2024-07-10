package cn.piesat.tests.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * <p/>
 * {@code @description}  :启动程序
 * <p/>
 * <b>@create:</b> 2022/11/9 17:26.
 *
 * @author zhouxp
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"cn.piesat.framework.log.external.client"})
public class LogExternalApplication {
    public static void main(String[] args) {
        SpringApplication.run(LogExternalApplication.class,args);
    }
}
