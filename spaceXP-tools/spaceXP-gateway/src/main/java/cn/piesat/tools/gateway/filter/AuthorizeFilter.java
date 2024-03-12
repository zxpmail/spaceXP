package cn.piesat.tools.gateway.filter;


import cn.piesat.framework.common.constants.CommonConstants;
import cn.piesat.framework.common.model.dto.JwtUser;
import cn.piesat.framework.common.model.enums.CommonResponseEnum;
import cn.piesat.framework.common.model.vo.ApiResult;
import cn.piesat.framework.common.utils.JwtUtils;
import cn.piesat.framework.redis.core.RedisService;
import cn.piesat.tools.gateway.constant.GatewayConstant;
import cn.piesat.tools.gateway.properties.GatewayProperties;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author zhouxp
 */
@Slf4j
public class AuthorizeFilter implements GlobalFilter, Ordered {

    private final GatewayProperties gatewayProperties;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Resource
    private RedisService redisService;

    public AuthorizeFilter(GatewayProperties gatewayProperties) {
        this.gatewayProperties = gatewayProperties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if(gatewayProperties.getIsAuthentication()){
            return chain.filter(exchange);
        }
        ServerHttpRequest request = exchange.getRequest();

        ServerHttpResponse response = exchange.getResponse();
        String uri = request.getURI().getPath();
        String ip = analysisSourceIp(request);
        ServerHttpRequest.Builder header = request.mutate().header(GatewayConstant.IP, ip);
        if (!StringUtils.hasText(uri)) {
            return chain.filter(exchange.mutate().request(header.build()).build());
        }
        String[] split = StringUtils.tokenizeToStringArray(uri, "/");
        if (split.length < 1) {
            return chain.filter(exchange.mutate().request(header.build()).build());
        }
        String sass = split[0];
        String path = uri.substring(sass.length() + 1);
        ServerHttpRequest mutableReq = null;
        //  检查白名单（配置）
        if (!CollectionUtils.isEmpty(gatewayProperties.getIgnorePaths())) {
            for (String ignorePath : gatewayProperties.getIgnorePaths()) {
                if (PATH_MATCHER.match(ignorePath,uri)) {
                    mutableReq = header
                            .header(CommonConstants.URI, path)
                            .header(CommonConstants.SASS, sass)
                            .build();
                    return chain.filter(exchange.mutate().request(mutableReq).build());
                }
            }
        }
        String token = request.getHeaders().getFirst(CommonConstants.TOKEN);
        if (!StringUtils.hasText(token)) {
            token = request.getQueryParams().getFirst(CommonConstants.TOKEN);
        }
        if(gatewayProperties.getIsRedirect()){
            if (!StringUtils.hasText(token)||"''".equalsIgnoreCase(token)) {
               return gotoLoginPage(response);
            }
        }else {
            if (!StringUtils.hasText(token)||"''".equalsIgnoreCase(token)) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return getVoidMono(response);
            }
        }

        Object userId =null;
        try {
            userId = JwtUtils.getValue(token, gatewayProperties.getTokenProperties().getTokenSignKey());
        } catch (Exception ex) {
            return getVoidMono(response);
        }
        //保证同一用户登录在不同窗口登录一次
        Object checkToken = redisService.getObject(gatewayProperties.getTokenProperties().getLoginToken() + "_check_" + userId);
        if (ObjectUtils.isEmpty(checkToken) || !token.equalsIgnoreCase(checkToken.toString())) {
            return getVoidMono(response);
        }

        Object object = redisService.getObject(gatewayProperties.getTokenProperties().getLoginToken() + userId);
        JwtUser user = null;
        try {
            user = JSON.parseObject(object.toString(), JwtUser.class);
        } catch (Exception ex) {
            return getVoidMono(response);
        }


        redisService.expire(gatewayProperties.getTokenProperties().getLoginToken() + user.getUserId(), gatewayProperties.getTokenProperties().getExpiration());

        try {
            mutableReq = header.header(CommonConstants.USER_ID, user.getUserId().toString())
                    .header(CommonConstants.DEPT_ID, user.getDeptId().toString())
                    .header(CommonConstants.TENANT_ID, user.getTenantId().toString())
                    .header(CommonConstants.USERNAME, URLEncoder.encode(user.getUserName(), StandardCharsets.UTF_8.toString()))
                    .header(CommonConstants.DEPT_NAME, URLEncoder.encode(user.getDeptName(), StandardCharsets.UTF_8.toString()))
                    .header(CommonConstants.URI, path)
                    .header(CommonConstants.SASS, sass)
                    .build();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        ServerWebExchange mutableExchange = exchange.mutate().request(mutableReq).build();
        return chain.filter(mutableExchange);
    }

    private Mono<Void>gotoLoginPage(ServerHttpResponse response){
        response.getHeaders().set(HttpHeaders.LOCATION,gatewayProperties.getRedirectUrl() );
        response.setStatusCode(HttpStatus.SEE_OTHER);
        response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
        return response.setComplete();
    }

    private Mono<Void> getVoidMono(ServerHttpResponse response) {
        response.getHeaders().add(GatewayConstant.HEADER_NAME, GatewayConstant.HEADER_VALUE);
        try {
            response.setStatusCode(HttpStatus.OK);
            DataBuffer dataBuffer = response.bufferFactory()
                    .wrap(objectMapper.writeValueAsString(ApiResult.fail(CommonResponseEnum.TOKEN_INVALID)).getBytes());
            return response.writeWith(Flux.just(dataBuffer));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error(CommonConstants.MESSAGE, GatewayConstant.MODULE, e.getMessage());
            return null;
        }

    }

    @Override
    public int getOrder() {
        return -100;
    }

    /**
     * 获取客户端真实IP
     */
    private String analysisSourceIp(ServerHttpRequest request) {
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

}
