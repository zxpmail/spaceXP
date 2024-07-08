package cn.piesat.tools.log.biz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class LogBizApplication {

    public static void main(String[] args) {
        System.setProperty("spring.cloud.bootstrap.enabled","true");
        SpringApplication.run(LogBizApplication.class, args);
    }

}
