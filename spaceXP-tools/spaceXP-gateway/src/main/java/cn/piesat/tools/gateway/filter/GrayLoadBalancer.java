package cn.piesat.tools.gateway.filter;

import cn.piesat.tools.gateway.properties.GatewayProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

/**
 * <p/>
 * {@code @description}: 灰度实现类
 * <p/>
 * {@code @create}: 2024-12-10 16:06
 * {@code @author}: zhouxp
 */
@Slf4j
public class GrayLoadBalancer implements ReactorServiceInstanceLoadBalancer {
    private final GatewayProperties.Gray gray;
    /**
     * 用于获取 serviceId 对应的服务实例的列表
     */
    private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;
    /**
     * 需要获取的服务实例名
     * <p>
     * 暂时用于打印 logger 日志
     */
    private final String serviceId;

    public GrayLoadBalancer(GatewayProperties.Gray gray, ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId) {
        this.gray = gray;
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        this.serviceId = serviceId;
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        return null;
    }
}
