package cn.piesat.tools.generator.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p/>
 * {@code @description}  :跨域资源共享
 * <p/>
 * <b>@create:</b> 2023/12/15 11:23.
 *
 * @author zhouxp
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 允许所有域名进行跨域访问
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true) // 是否允许发送Cookie
                .maxAge(3600); // 预检间隔时间，单位为秒
    }
}
