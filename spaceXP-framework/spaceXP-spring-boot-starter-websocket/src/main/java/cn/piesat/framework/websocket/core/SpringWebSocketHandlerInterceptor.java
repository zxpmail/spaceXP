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
    private final  Pattern pattern = Pattern.compile("^-?\\d+$");
    private final WebSocketProperties webSocketProperties;

    @Autowired(required = false)
    private GroupMemberService groupMemberService;

    public SpringWebSocketHandlerInterceptor(WebSocketProperties webSocketProperties) {
        this.webSocketProperties = webSocketProperties;
    }

    @Override
    public boolean beforeHandshake(@Nonnull ServerHttpRequest request, @Nonnull ServerHttpResponse response, @Nonnull WebSocketHandler wsHandler, @Nonnull Map<String, Object> attributes) throws Exception {
        super.beforeHandshake(request, response, wsHandler, attributes);
        if (request instanceof ServletServerHttpRequest) {
            String userId = WebsocketConstant.DEBUG_USER_ID;
            Integer appId = WebsocketConstant.DEBUG_USER_APPID;
            if (!webSocketProperties.getDebug()) {
                userId = request.getHeaders().getFirst(CommonConstants.USER_ID);
                if (!StringUtils.hasText(userId)) {
                    log.info("Attempted to get a Header with a null userId.");
                    return false;
                }
                String rAppId = request.getHeaders().getFirst(CommonConstants.APP_ID);

                if (!StringUtils.hasText(rAppId)||pattern.matcher(rAppId).matches() ){
                    log.info("Attempted to get a Header with a null  or no number appId.");
                    return false;
                }
                appId =Integer.parseInt(rAppId);
                groupMemberService.addUser2Group(userId);
            }
            HttpSession session = ((ServletServerHttpRequest) request).getServletRequest().getSession(true);
            if (session != null) {
                attributes.put(CommonConstants.USER_ID, userId);
                attributes.put(CommonConstants.APP_ID, appId);
                return true;
            }
        }
        return false;
    }
}
