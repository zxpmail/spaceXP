package cn.piesat.tools.gateway.filter;

import cn.piesat.tools.gateway.properties.GatewayProperties;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * <p/>
 * {@code @description}: 灰度发布拦截器
 * <p/>
 * {@code @create}: 2024-12-10 15:16
 * {@code @author}: zhouxp
 */
public class GrayFilter implements GlobalFilter, Ordered {


    private final GatewayProperties.Gray gray;

    public GrayFilter(GatewayProperties.Gray gray) {

        this.gray = gray;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return null;
    }

    @Override
    public int getOrder() {
        return ReactiveLoadBalancerClientFilter.LOAD_BALANCER_CLIENT_FILTER_ORDER;
    }
}
