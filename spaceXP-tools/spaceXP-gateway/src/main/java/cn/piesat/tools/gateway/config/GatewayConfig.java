package cn.piesat.tools.gateway.config;


import cn.piesat.tools.gateway.filter.AuthorizeFilter;
import cn.piesat.tools.gateway.properties.GatewayProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p/>
 * {@code @description}  :网关配置类
 * <p/>
 * <b>@create:</b> 2023/10/8 8:56.
 *
 * @author zhouxp
 */
@Configuration
@EnableConfigurationProperties({GatewayProperties.class})
public class GatewayConfig {
    @Bean
    public AuthorizeFilter authorizeFilter(GatewayProperties gatewayProperties){
        return  new AuthorizeFilter(gatewayProperties);
    }
}
