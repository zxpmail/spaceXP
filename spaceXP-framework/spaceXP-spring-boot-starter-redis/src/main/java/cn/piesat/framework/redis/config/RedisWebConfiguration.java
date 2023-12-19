package cn.piesat.framework.redis.config;

import cn.piesat.framework.redis.core.AccessLimitInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p/>
 * {@code @description}  :redis web拦截器类
 * <p/>
 * <b>@create:</b> 2023/12/19 16:10.
 *
 * @author zhouxp
 */
public class RedisWebConfiguration implements WebMvcConfigurer {
    private final AccessLimitInterceptor accessLimitInterceptor;

    public RedisWebConfiguration(AccessLimitInterceptor accessLimitInterceptor) {
        this.accessLimitInterceptor = accessLimitInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessLimitInterceptor);
        WebMvcConfigurer.super.addInterceptors(registry);
    }

}
