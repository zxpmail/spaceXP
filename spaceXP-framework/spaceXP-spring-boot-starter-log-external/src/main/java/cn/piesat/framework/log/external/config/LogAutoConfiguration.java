package cn.piesat.framework.log.external.config;


import cn.piesat.framework.log.external.properties.ToolsLogProperties;
import cn.piesat.framework.log.external.service.LogExecuteService;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.client.RestTemplate;

/**
 * <p/>
 * {@code @description}  :日志自动配置类
 * <p/>
 * <b>@create:</b> 2023/10/17 10:05.
 *
 * @author zhouxp
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties(ToolsLogProperties.class)
public class LogAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate geRestTemplate(){
        return new RestTemplate();
    }
    @Bean
    public LogExecuteService logExecuteService(ToolsLogProperties toolsLogProperties, RestTemplate restTemplate) {
        return new LogExecuteService(toolsLogProperties,restTemplate);
    }
}
