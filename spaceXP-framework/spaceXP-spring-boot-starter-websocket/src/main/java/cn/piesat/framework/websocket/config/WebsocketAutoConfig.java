package cn.piesat.framework.websocket.config;

import cn.piesat.framework.websocket.core.SpringWebSocketHandler;
import cn.piesat.framework.websocket.core.SpringWebSocketHandlerInterceptor;
import cn.piesat.framework.websocket.properties.WebSocketProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p/>
 * {@code @description}: 自动配置类
 * <p/>
 * {@code @create}: 2024-06-19 14:11
 * {@code @author}: zhouxp
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({WebSocketProperties.class})
public class WebsocketAutoConfig {
    @Bean
    public SpringWebSocketHandlerInterceptor springWebSocketHandlerInterceptor(WebSocketProperties webSocketProperties){
        return new SpringWebSocketHandlerInterceptor(webSocketProperties);
    }
    @Bean
    public SpringWebSocketHandler springWebSocketHandler(){
        return  new SpringWebSocketHandler();
    }
}
