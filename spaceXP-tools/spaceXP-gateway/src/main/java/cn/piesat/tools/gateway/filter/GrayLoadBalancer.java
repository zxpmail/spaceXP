package cn.piesat.tools.gateway.filter;

import cn.piesat.framework.common.constants.CommonConstants;
import cn.piesat.tools.gateway.properties.GatewayProperties;
import com.alibaba.cloud.nacos.balancer.NacosBalancer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.RequestDataContext;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.http.HttpHeaders;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static cn.piesat.tools.gateway.constant.GatewayConstant.VERSION;

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

    private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;
    /**
     * 服务实例名
     */
    private final String serviceId;

    public GrayLoadBalancer(GatewayProperties.Gray gray, ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId) {
        this.gray = gray;
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        this.serviceId = serviceId;
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {

        HttpHeaders headers = ((RequestDataContext) request.getContext()).getClientRequest().getHeaders();
        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider.getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next().map(list -> getInstanceResponse(list, headers));
    }

    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances, HttpHeaders headers) {
        String userId = headers.getFirst(CommonConstants.USER_ID);

        String grayVersion = gray.getGrayVersion();
        String version = gray.getVersion();

        if (CollectionUtils.isEmpty(instances)||!StringUtils.hasText(userId)||!StringUtils.hasText(grayVersion)||!StringUtils.hasText(version) ) {
            log.warn("有空数据：serviceId:{},userId:{},grayVersion:{},version:{}", serviceId,userId,grayVersion,version);
            return new EmptyResponse();
        }

        List<String> grayUsers = gray.getUsers();
        List<ServiceInstance> chooseInstances;
        if (!grayUsers.contains(userId)) {
            // 正式版本
            chooseInstances = filterList(instances, instance -> version.equals(instance.getMetadata().get(VERSION)));
        } else {
            // 满足用户的灰度版本
            chooseInstances = filterList(instances, instance -> grayVersion.equals(instance.getMetadata().get(VERSION)) && grayUsers.contains(userId));
            if (CollectionUtils.isEmpty(chooseInstances)) {
                log.warn("[serviceId:{} 没有满足grayVersion:{}的服务，使用所有服务实例列表]", serviceId, grayVersion);
                chooseInstances = instances;
            }
        }

        // 随机 + 权重获取实例列表
        return new DefaultResponse(NacosBalancer.getHostByRandomWeight3(chooseInstances));
    }

    private static <T> List<T> filterList(Collection<T> instances, Predicate<T> predicate) {
        if (CollectionUtils.isEmpty(instances)) {
            return new ArrayList<>();
        }
        return instances.stream().filter(predicate).collect(Collectors.toList());
    }
}
