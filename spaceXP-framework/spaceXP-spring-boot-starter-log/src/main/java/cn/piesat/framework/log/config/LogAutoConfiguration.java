package cn.piesat.framework.log.config;

import cn.piesat.framework.common.properties.ModuleProperties;
import cn.piesat.framework.log.core.OpLogAspect;
import cn.piesat.framework.log.core.SwaggerLogAspect;
import cn.piesat.framework.log.event.LogListener;
import cn.piesat.framework.log.properties.LogProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p/>
 * {@code @description}  :使用日志标识生成拦截器
 * <p/>
 * <b>@create:</b> 2022/12/9 16:13.
 * @author zhouxp
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({LogProperties.class, ModuleProperties.class})
public class LogAutoConfiguration implements WebMvcConfigurer {

    @Bean
    @ConditionalOnProperty(name = "space.log.log-flag", havingValue = "2",matchIfMissing = false)
    public SwaggerLogAspect swaggerLogAspect(LogProperties logProperties, ModuleProperties moduleProperties){
        return new SwaggerLogAspect(logProperties,moduleProperties.getModule());
    }

    @Bean
    @ConditionalOnProperty(name = "space.log.log-flag", havingValue = "1",matchIfMissing = true)
    public OpLogAspect opLogAspect(LogProperties logProperties,ModuleProperties moduleProperties){
        return new OpLogAspect(logProperties,moduleProperties.getModule());
    }
    @Bean
    public LogListener logListener(){
        return new LogListener();
    }
}
