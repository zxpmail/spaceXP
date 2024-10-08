package cn.piesat.framework.websocket.core;

import cn.piesat.framework.common.constants.CommonConstants;
import cn.piesat.framework.websocket.model.WebsocketConstant;
import cn.piesat.framework.websocket.properties.WebSocketProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpSession;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * <p/>
 * {@code @description}: 握手拦截器
 * <p/>
 * {@code @create}: 2024-06-18 15:58
 * {@code @author}: zhouxp
 */
@Slf4j

public class SpringWebSocketHandlerInterceptor extends HttpSessionHandshakeInterceptor {
    private final Pattern pattern = Pattern.compile("^-?\\d+$");
    private final WebSocketProperties webSocketProperties;

    @Autowired(required = false)
    private CallbackService callbackService;

    public SpringWebSocketHandlerInterceptor(WebSocketProperties webSocketProperties) {
        this.webSocketProperties = webSocketProperties;
    }

    @Override
    public boolean beforeHandshake(@Nonnull ServerHttpRequest request, @Nonnull ServerHttpResponse response, @Nonnull WebSocketHandler wsHandler, @Nonnull Map<String, Object> attributes) throws Exception {
        super.beforeHandshake(request, response, wsHandler, attributes);
        if (request instanceof ServletServerHttpRequest) {
            Long userId = WebsocketConstant.DEBUG_USER_ID;
            Integer appId = WebsocketConstant.DEBUG_USER_APPID;
            String ip = WebsocketConstant.IP;
            if (!webSocketProperties.getDebug()) {
                String uId = request.getHeaders().getFirst(CommonConstants.USER_ID);
                if (!StringUtils.hasText(uId) || !pattern.matcher(uId).matches()) {
                    log.info("Attempted to get a Header with a null userId.");
                    return false;
                }
                userId = Long.parseLong(uId);
                String rAppId = request.getHeaders().getFirst(CommonConstants.APP_ID);
                ip = request.getHeaders().getFirst(CommonConstants.IP);
                if (!StringUtils.hasText(rAppId) || !pattern.matcher(rAppId).matches()) {
                    log.info("Attempted to get a Header with a null  or no number appId.");
                    return false;
                }
                appId = Integer.parseInt(rAppId);
                if (callbackService != null) {
                    callbackService.addUser2Group(userId, appId);
                }

            }
            HttpSession session = ((ServletServerHttpRequest) request).getServletRequest().getSession(true);

            if (session != null) {
                attributes.put(CommonConstants.USER_ID, userId);
                attributes.put(CommonConstants.APP_ID, appId);
                attributes.put(CommonConstants.IP, ip);
                return true;
            }
        }
        return false;
    }
}
