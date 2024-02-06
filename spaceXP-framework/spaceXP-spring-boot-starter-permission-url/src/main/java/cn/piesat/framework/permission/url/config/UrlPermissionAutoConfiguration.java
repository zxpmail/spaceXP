package cn.piesat.framework.permission.url.config;



import cn.piesat.framework.permission.url.core.UrlPermissionFilter;
import cn.piesat.framework.permission.url.properties.UrlPermissionProperties;
import jakarta.servlet.Filter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;



/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2023/9/12 8:53.
 *
 * @author zhouxp
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({UrlPermissionProperties.class})
@Primary
public class UrlPermissionAutoConfiguration {
    @Bean
    public FilterRegistrationBean<Filter> filterRegistration(Filter urlPermissionFilter, UrlPermissionProperties urlPermissionProperties) {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(urlPermissionFilter);
        registrationBean.addUrlPatterns(urlPermissionProperties.getUrlPatterns());
        registrationBean.setName(urlPermissionProperties.getName());
        //设置优先级，数越小优先级越高
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public Filter urlPermissionFilter(UrlPermissionProperties urlPermissionProperties) {
        return new UrlPermissionFilter(urlPermissionProperties);
    }
}
