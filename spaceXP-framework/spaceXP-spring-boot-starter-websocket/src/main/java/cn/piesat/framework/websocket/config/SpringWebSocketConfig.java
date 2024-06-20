package cn.piesat.framework.websocket.config;


import cn.piesat.framework.websocket.core.SpringWebSocketHandler;
import cn.piesat.framework.websocket.core.SpringWebSocketHandlerInterceptor;
import cn.piesat.framework.websocket.model.WebsocketConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistration;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p/>
 * {@code @description}: web自动配置类
 * <p/>
 * {@code @create}: 2024-06-19 10:09
 * {@code @author}: zhouxp
 */
@Configuration(proxyBeanMethods = false)
@EnableWebSocket
@DependsOn({"springWebSocketHandler","springWebSocketHandlerInterceptor"})
public class SpringWebSocketConfig implements WebSocketConfigurer {

    @Value("${space.websocket.allowedOrigin:false}")
    private Boolean allowedOrigin;

    @Value("#{'space.websocket.domainNames'.split(',')}")
    private List<String> domainNames;

    @Value("${space.websocket.handlerPath:/ws}")
    private String handlerPath;
    @Resource
    private SpringWebSocketHandler springWebSocketHandler;
    @Resource
    private SpringWebSocketHandlerInterceptor springWebSocketHandlerInterceptor;


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        WebSocketHandlerRegistration webSocketHandlerRegistration = registry.addHandler(springWebSocketHandler, handlerPath)
                .addInterceptors(springWebSocketHandlerInterceptor);
        if (allowedOrigin) {
            if (CollectionUtils.isEmpty(domainNames)) {
                webSocketHandlerRegistration.setAllowedOrigins(WebsocketConstant.DEFAULT_DOMAIN);
            } else {
                webSocketHandlerRegistration.setAllowedOrigins(domainNames.toArray(new String[0]));
            }
        }

    }
}

