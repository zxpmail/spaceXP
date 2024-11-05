package cn.piesat.framework.log.config;


import cn.piesat.framework.log.core.OpLogAspect;
import cn.piesat.framework.log.core.SwaggerLogAspect;
import cn.piesat.framework.log.event.LogListener;
import cn.piesat.framework.log.external.Log4j2DynamicLoggingConfigurer;
import cn.piesat.framework.log.external.LogbackDynamicLoggingConfigurer;
import cn.piesat.framework.log.properties.LogProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p/>
 * {@code @description}  :使用日志标识生成拦截器
 * <p/>
 * <b>@create:</b> 2022/12/9 16:13.
 * @author zhouxp
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({LogProperties.class})
public class LogAutoConfiguration implements WebMvcConfigurer {
    @Value("${spring.application.name:test}")
    private String module;
    @Bean
    @ConditionalOnProperty(name = "space.log.log-flag", havingValue = "2",matchIfMissing = false)
    public SwaggerLogAspect swaggerLogAspect(LogProperties logProperties){
        return new SwaggerLogAspect(logProperties,module);
    }

    @Bean
    @ConditionalOnProperty(name = "space.log.log-flag", havingValue = "1",matchIfMissing = true)
    public OpLogAspect opLogAspect(LogProperties logProperties){
        return new OpLogAspect(logProperties,module);
    }
    @Bean
    public LogListener logListener(){
        return new LogListener();
    }

    @Bean
    @ConditionalOnClass(name = "ch.qos.logback.classic.LoggerContext")
    public LogbackDynamicLoggingConfigurer logbackDynamicLoggingConfigurer() {
        return new LogbackDynamicLoggingConfigurer();
    }

    @Bean
    @ConditionalOnMissingClass("ch.qos.logback.classic.LoggerContext")
    @ConditionalOnClass(name = "org.apache.logging.log4j.core.LoggerContext")
    public Log4j2DynamicLoggingConfigurer log4j2DynamicLoggingConfigurer() {
        return new Log4j2DynamicLoggingConfigurer();
    }
}
