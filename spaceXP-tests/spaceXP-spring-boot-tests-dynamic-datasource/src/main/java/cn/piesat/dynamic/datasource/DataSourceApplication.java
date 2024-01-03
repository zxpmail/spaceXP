package cn.piesat.dynamic.datasource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

/**
 * <p/>
 * {@code @description}  :启动程序
 * <p/>
 * <b>@create:</b> 2022/11/9 17:26.
 *
 * @author zhouxp
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class })
public class DataSourceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataSourceApplication.class,args);
    }
}
