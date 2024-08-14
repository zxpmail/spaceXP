package cn.piesat.framework.common.config;

import cn.piesat.framework.common.aspect.ServiceValidationAspect;
import cn.piesat.framework.common.properties.CommonProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p/>
 * {@code @description}: 通用自动配置类
 * <p/>
 * {@code @create}: 2024-08-12 11:09
 * {@code @author}: zhouxp
 */
@Configuration(proxyBeanMethods = false)
public class CommonAutoConfig {
    @ConditionalOnProperty(name = "space.common.service-validation-enable", havingValue = "true")
    @Bean
    public ServiceValidationAspect getServiceValidationAspect() {
        return new ServiceValidationAspect();
    }

    @Bean("commonProperties")
    public CommonProperties getCommonProperties(){
        return  new CommonProperties();
    }
}
