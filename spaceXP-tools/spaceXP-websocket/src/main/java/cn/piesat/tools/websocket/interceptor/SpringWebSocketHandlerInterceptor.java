package cn.piesat.tools.websocket.interceptor;

import cn.piesat.framework.common.constants.CommonConstants;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * <p/>
 * {@code @description}  :拦截器
 * <p/>
 * <b>@create:</b> 2024-05-31 16:13.
 *
 * @author zhouxp
 */
@Component
public class SpringWebSocketHandlerInterceptor extends HttpSessionHandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        super.beforeHandshake(request,response,wsHandler,attributes);
        if (request instanceof ServletServerHttpRequest) {
            String userId = request.getHeaders().getFirst(CommonConstants.USER_ID);

            if(!StringUtils.hasText(userId)){
                return false;
            }
            HttpSession session = ((ServletServerHttpRequest) request).getServletRequest().getSession(true);
            if (session != null) {
                attributes.put(CommonConstants.USER_ID,userId);
                return true;
            }
        }
        return false;
    }
}
