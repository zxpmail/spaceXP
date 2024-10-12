package cn.piesat.framework.web.config;


import cn.piesat.framework.web.core.LoginUserHandlerMethodArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;
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

    /**
     * 交换MappingJackson2HttpMessageConverter与第一位元素
     * 让返回值类型为String的接口能正常返回包装结果
     *
     * @param converters initially an empty list of converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 确保列表不为空
        if (converters.isEmpty()) {
            return;
        }
        // 找到第一个 MappingJackson2HttpMessageConverter 并与第一个元素交换位置
        for (int i = 0; i < converters.size(); i++) {
            if (converters.get(i) instanceof MappingJackson2HttpMessageConverter) {
                if (i != 0) {
                    Collections.swap(converters, 0, i);
                }
                break;
            }
        }
    }
}
