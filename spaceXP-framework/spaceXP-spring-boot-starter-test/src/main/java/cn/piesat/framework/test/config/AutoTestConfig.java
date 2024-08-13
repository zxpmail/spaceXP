package cn.piesat.framework.test.config;

import cn.piesat.framework.test.core.AutoSetPojo;
import cn.piesat.framework.test.properties.TestProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p/>
 * {@code @description}: 测试配置类
 * <p/>
 * {@code @create}: 2024-08-13 9:41
 * {@code @author}: zhouxp
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({TestProperties.class})
public class AutoTestConfig {
    @Bean
    public  AutoSetPojo getAutoSetPojo(TestProperties testProperties) {
        return new AutoSetPojo(testProperties);
    }
}
