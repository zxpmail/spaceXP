package cn.piesat.framework.web.config;


import cn.piesat.framework.web.core.LoginUserHandlerMethodArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * <p/>
 * {@code @description}  :在webMvc增加loginUser绑定类
 * <p/>
 * <b>@create:</b> 2022/12/6 14:04.
 *
 * @author zhouxp
 */
@RequiredArgsConstructor
public class WebMvcConfigArg implements WebMvcConfigurer {
    private final LoginUserHandlerMethodArgumentResolver loginUserHandlerMethodArgumentResolver;
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(loginUserHandlerMethodArgumentResolver);
    }
}
