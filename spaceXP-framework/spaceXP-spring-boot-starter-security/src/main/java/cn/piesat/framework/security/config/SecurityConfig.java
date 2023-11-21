package cn.piesat.framework.security.config;

import cn.piesat.framework.security.core.EncryptAspect;
import cn.piesat.framework.security.properties.SecurityProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p/>
 * {@code @description}  :安全配置功能
 * <p/>
 * <b>@create:</b> 2023/1/11 8:36.
 *
 * @author zhouxp
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfig {
    @Bean
    @ConditionalOnProperty(name = "space.security.enable-encrypt-aspect", havingValue = "true",matchIfMissing = false)
    public EncryptAspect encryptAspect(SecurityProperties securityProperties){
        return new EncryptAspect(securityProperties.getSecretKey());
    }
}
