package cn.piesat.framework.security.config;

import cn.piesat.framework.security.core.LicenseCheckInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p/>
 * {@code @description}:
 * <p/>
 * {@code @create}: 2024-10-24 16:41
 * {@code @author}: zhouxp
 */
public class LicenseWebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LicenseCheckInterceptor());
    }
}
