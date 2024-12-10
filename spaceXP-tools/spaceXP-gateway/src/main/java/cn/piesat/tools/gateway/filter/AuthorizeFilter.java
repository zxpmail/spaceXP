package cn.piesat.tools.gateway.filter;


import cn.piesat.framework.common.constants.CommonConstants;
import cn.piesat.framework.common.model.dto.JwtUser;
import cn.piesat.framework.common.model.enums.CommonResponseEnum;
import cn.piesat.framework.common.model.interfaces.IBaseResponse;
import cn.piesat.framework.common.model.vo.ApiResult;
import cn.piesat.framework.common.utils.JwtUtils;
import cn.piesat.framework.redis.core.RedisService;
import cn.piesat.tools.gateway.constant.GatewayConstant;
import cn.piesat.tools.gateway.model.enums.GatewayResponseEnum;
import cn.piesat.tools.gateway.properties.GatewayProperties;
import cn.piesat.tools.gateway.utils.GatewayUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author zhouxp
 */
@Slf4j
public class AuthorizeFilter implements GlobalFilter, Ordered {

    private final GatewayProperties gatewayProperties;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Resource
    private RedisService redisService;

    public AuthorizeFilter(GatewayProperties gatewayProperties) {
        this.gatewayProperties = gatewayProperties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!gatewayProperties.getLogin().getIsAuthentication()) {
            return chain.filter(exchange);
        }
        ServerHttpRequest request = exchange.getRequest();

        ServerHttpResponse response = exchange.getResponse();
        String uri = request.getURI().getPath();
        String ip = GatewayUtil.analysisSourceIp(request);
        ServerHttpRequest.Builder header = request.mutate().header(CommonConstants.IP, ip);
        if (!StringUtils.hasText(uri)) {
            return chain.filter(exchange.mutate().request(header.build()).build());
        }
        String[] split = StringUtils.tokenizeToStringArray(uri, "/");
        if (split.length < 1) {
            return chain.filter(exchange.mutate().request(header.build()).build());
        }
        String service = split[0];
        String path = uri.substring(service.length() + 1);
        ServerHttpRequest mutableReq;
        //  检查白名单（配置）
        if(GatewayUtil.isIgnoredPatterns(exchange,gatewayProperties.getLogin().getIgnorePaths())){
            mutableReq = header
                    .header(CommonConstants.URI, path)
                    .header(CommonConstants.SASS, service)
                    .build();
            return chain.filter(exchange.mutate().request(mutableReq).build());
        }
        String token = request.getHeaders().getFirst(CommonConstants.TOKEN);
        if (!StringUtils.hasText(token)) {
            token = request.getQueryParams().getFirst(CommonConstants.TOKEN);
        }
        if (!StringUtils.hasText(token)) {
            token = request.getHeaders().getFirst(GatewayConstant.WS_TOKEN);
        }
        if (gatewayProperties.getLogin().getIsRedirect()) {
            if (!StringUtils.hasText(token) || "''".equalsIgnoreCase(token)) {
                log.error("redirect");
                return getVoidMono(response, GatewayResponseEnum.REDIRECT);
            }
        } else {
            if (!StringUtils.hasText(token) || "''".equalsIgnoreCase(token)) {
                log.error("token is null!!");
                return getVoidMono(response, CommonResponseEnum.TOKEN_INVALID);
            }
        }

        Object userId;
        try {
            userId = JwtUtils.getValue(token, gatewayProperties.getLogin().getTokenProperties().getTokenSignKey());
        } catch (Exception ex) {
            log.error("token转化错误！！");
            return getVoidMono(response, CommonResponseEnum.TOKEN_INVALID);
        }
        //保证同一用户登录在不同窗口登录一次
        String checkToken = redisService.getObject(gatewayProperties.getLogin().getTokenProperties().getLoginToken() + "_check_" + userId);
        if (ObjectUtils.isEmpty(checkToken) || !token.equalsIgnoreCase(checkToken)) {
            log.error("{} token is invalid!!!",checkToken);
            return getVoidMono(response, CommonResponseEnum.TOKEN_INVALID);
        }

        Object object = redisService.getObject(gatewayProperties.getLogin().getTokenProperties().getLoginToken() + userId);
        JwtUser user;
        try {
            assert object != null;
            user = JSON.parseObject(object.toString(), JwtUser.class);
        } catch (Exception ex) {
            log.error("parse user fail !!!");
            return getVoidMono(response, CommonResponseEnum.TOKEN_INVALID);
        }

        redisService.expire(gatewayProperties.getLogin().getTokenProperties().getLoginToken() + user.getUserId(), gatewayProperties.getLogin().getTokenProperties().getExpiration());


        mutableReq = header.header(CommonConstants.USER_ID, user.getUserId().toString())
                .header(CommonConstants.DEPT_ID, user.getDeptId().toString())
                .header(CommonConstants.TENANT_ID, user.getTenantId().toString())
                .header(CommonConstants.USERNAME, encode(user.getUserName()))
                .header(CommonConstants.DEPT_NAME, encode(user.getDeptName()))
                .header(CommonConstants.URI, path)
                .header(CommonConstants.SASS, service)
                .header(CommonConstants.APP_ID, GatewayConstant.APP_ID)
                .build();


        ServerWebExchange mutableExchange = exchange.mutate().request(mutableReq).build();
        return chain.filter(mutableExchange);
    }

    private String encode(String name) {
        if (!StringUtils.hasText(name)) {
            return GatewayConstant.defaultName;
        }
        try {
            return URLEncoder.encode(name, GatewayConstant.UTF8);
        } catch (UnsupportedEncodingException e) {
            log.error("{} Encoding not supported: UTF-8", name, e);
            throw new RuntimeException(e);
        }
    }

    private Mono<Void> getVoidMono(ServerHttpResponse response, IBaseResponse iBaseResponse) {
        response.getHeaders().add(GatewayConstant.HEADER_NAME, GatewayConstant.HEADER_VALUE);
        response.setStatusCode(HttpStatus.OK);
        try {
            String responseString = objectMapper.writeValueAsString(ApiResult.fail(iBaseResponse.getCode(), iBaseResponse.getMessage()));
            // 设置响应体，并确保资源管理
            return response.writeWith(Flux.just(response.bufferFactory().wrap(responseString.getBytes())));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error(CommonConstants.MESSAGE, GatewayConstant.MODULE, e.getMessage());
            return Mono.error(e);
        }
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
