package cn.piesat.permission.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * <p/>
 * {@code @description}  :启动程序
 * <p/>
 * <b>@create:</b> 2022/11/9 17:26.
 *
 * @author zhouxp
 */
@SpringBootApplication
@EnableScheduling
public class PermissionDataApplication {
    public static void main(String[] args) {
        SpringApplication.run(PermissionDataApplication.class,args);
    }
}
