package cn.piesat.tools.gateway.utils;

import cn.piesat.tools.gateway.constant.GatewayConstant;
import cn.piesat.tools.gateway.model.GatewayLog;
import cn.piesat.tools.gateway.properties.GatewayProperties;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;
import java.util.List;
import java.util.Objects;

/**
 * <p/>
 * {@code @description}: IP工具类
 * <p/>
 * {@code @create}: 2024-11-19 17:17
 * {@code @author}: zhouxp
 */
@Slf4j
public class GatewayUtil {
    private static final String DEFAULT_VALUE = "N/A";
    private static final long DEFAULT_EXECUTE_TIME = -1;
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    /**
     * 获取客户端真实IP
     */
    public static String analysisSourceIp(ServerHttpRequest request) {
        String ip = null;
        //X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeaders().getFirst(GatewayConstant.X_FORWARDED_FOR);
        if (ipAddresses == null || ipAddresses.length() == 0 || GatewayConstant.UNKNOWN.equalsIgnoreCase(ipAddresses)) {
            //Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeaders().getFirst(GatewayConstant.PROXY_CLIENT_IP);
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || GatewayConstant.UNKNOWN.equalsIgnoreCase(ipAddresses)) {
            //WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeaders().getFirst(GatewayConstant.WL_PROXY_CLIENT_IP);
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || GatewayConstant.UNKNOWN.equalsIgnoreCase(ipAddresses)) {
            //HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeaders().getFirst(GatewayConstant.HTTP_CLIENT_IP);
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || GatewayConstant.UNKNOWN.equalsIgnoreCase(ipAddresses)) {
            //X-Real-IP：nginx服务代理
            ipAddresses = request.getHeaders().getFirst(GatewayConstant.X_REAL_IP);
        }
        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }
        //通过request.getRemoteAddr()获取IP
        if (ip == null || ip.length() == 0 || GatewayConstant.UNKNOWN.equalsIgnoreCase(ipAddresses)) {
            ip = Objects.requireNonNull(request.getRemoteAddress()).getHostString();
        }
        return ip;
    }
    /**
     * 校验白名单
     */
    public static Boolean isIgnoredPatterns(ServerWebExchange exchange,  List<String> ignoredPatterns) {
        if (CollectionUtils.isEmpty(ignoredPatterns)) {
            return Boolean.FALSE;
        }
        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        for (String pattern : ignoredPatterns) {
            if (PATH_MATCHER.match(pattern, uri.getPath())) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public static void printLog(GatewayLog gatewayLog){
        String ip = gatewayLog.getIp() != null ? gatewayLog.getIp() : DEFAULT_VALUE;
        String method = gatewayLog.getMethod() != null ? gatewayLog.getMethod() : DEFAULT_VALUE;
        String requestPath = gatewayLog.getRequestPath() != null ? gatewayLog.getRequestPath() : DEFAULT_VALUE;
        String targetServer = gatewayLog.getTargetServer() != null ? gatewayLog.getTargetServer() : DEFAULT_VALUE;
        Long executeTime = gatewayLog.getExecuteTime();
        String requestBody = gatewayLog.getRequestBody() != null ? gatewayLog.getRequestBody().replace("\n", "") : DEFAULT_VALUE;
        log.info("[{}] {} {}, route: {}, status: {}, execute: {} mills, requestBody: {}",
                ip,
                method,
                requestPath,
                targetServer,
                gatewayLog.getCode(),
                executeTime != null ? executeTime : DEFAULT_EXECUTE_TIME,
                requestBody
        );
    }
}
