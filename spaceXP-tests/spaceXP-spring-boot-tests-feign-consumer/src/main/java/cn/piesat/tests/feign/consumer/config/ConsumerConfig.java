package cn.piesat.tests.feign.consumer.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2023/11/17 11:06.
 *
 * @author zhouxp
 */
@Configuration
public class ConsumerConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> requestTemplate.header("Accept-Encoding", "gzip");
    }
}
