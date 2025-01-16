package cn.piesat.framework.security.config;

import cn.piesat.framework.security.core.EncryptAspect;
import cn.piesat.framework.security.core.InstallLicense;
import cn.piesat.framework.security.properties.SecurityProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
public class SecurityConfig  {
    @Bean
    @ConditionalOnProperty(name = "space.security.enable", havingValue = "true")
    public EncryptAspect encryptAspect(SecurityProperties securityProperties){
        return new EncryptAspect(securityProperties.getSecretKey(),securityProperties.getIv());
    }

    @Bean
    @ConditionalOnProperty(name = "space.security.license-enable", havingValue = "true")
    public InstallLicense installLicense(SecurityProperties securityProperties){
        return new InstallLicense(securityProperties.getLicense());
    }

    @Bean
    @ConditionalOnProperty(name = "space.security.license-enable", havingValue = "true")
    public LicenseWebMvcConfig licenseWebMvcConfig(){
        return new LicenseWebMvcConfig();
    }
}
