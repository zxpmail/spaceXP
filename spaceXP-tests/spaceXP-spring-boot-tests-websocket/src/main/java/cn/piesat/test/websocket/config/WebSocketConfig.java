package cn.piesat.test.websocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2024-06-11 9:13.
 *
 * @author zhouxp
 */
@Configuration
public class WebSocketConfig {

    /**
     * 注入一个ServerEndpointExporter,该Bean会自动注册使用@ServerEndpoint注解申明的websocket endpoint
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
