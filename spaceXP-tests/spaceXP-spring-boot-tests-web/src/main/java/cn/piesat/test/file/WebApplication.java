package cn.piesat.test.file;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * <p/>
 * {@code @description}  :启动程序
 * <p/>
 * <b>@create:</b> 2022/11/9 17:26.
 *
 * @author zhouxp
 */
@SpringBootApplication
public class WebApplication {
    @Bean
    public RestTemplate getRestTemplate(){

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setReadTimeout(5000); // 设置读取超时时间为5秒
        requestFactory.setConnectTimeout(5000); // 设置连接超时时间为5秒
        return new RestTemplate(requestFactory);
    }
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class,args);
    }
}
