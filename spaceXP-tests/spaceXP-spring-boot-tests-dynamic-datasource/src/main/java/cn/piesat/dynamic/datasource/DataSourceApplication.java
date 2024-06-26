package cn.piesat.dynamic.datasource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * <p/>
 * {@code @description}  :启动程序
 * <p/>
 * <b>@create:</b> 2022/11/9 17:26.
 *
 * @author zhouxp
 */
@SpringBootApplication
@EnableAsync
public class DataSourceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataSourceApplication.class,args);
    }
}
