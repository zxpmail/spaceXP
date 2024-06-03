package cn.piesat.tools.websocket.config;

import cn.piesat.tools.websocket.handler.SpringWebSocketHandler;
import cn.piesat.tools.websocket.interceptor.SpringWebSocketHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2024-05-31 16:39.
 *
 * @author zhouxp
 */
@Configuration
@EnableWebSocket
public class SpringWebSocketConfig implements WebSocketConfigurer {

    @Resource
    private SpringWebSocketHandler springWebSocketHandler;


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(springWebSocketHandler, "/ws")
                .setAllowedOrigins("*");
    }
}
