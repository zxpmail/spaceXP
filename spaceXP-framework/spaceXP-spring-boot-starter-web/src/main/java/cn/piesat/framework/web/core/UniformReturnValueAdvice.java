package cn.piesat.framework.web.core;

import cn.piesat.framework.common.annotation.NoApiResult;
import cn.piesat.framework.common.model.vo.ApiMapResult;
import cn.piesat.framework.common.model.vo.ApiResult;
import com.alibaba.fastjson2.JSON;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

/**
 * <p/>
 * {@code @description}  :统一返回值处理器
 * <p/>
 * <b>@create:</b> 2024/2/5 15:34.
 *
 * @author zhouxp
 */
@RestControllerAdvice
@SuppressWarnings("all")
public class UniformReturnValueAdvice implements ResponseBodyAdvice<Object> {

    private final List<String> ignorePackageOrClass;
    private final boolean apiMapResultEnable;

    public UniformReturnValueAdvice(List<String> ignorePackageOrClass, boolean apiMapResultEnable) {
        this.ignorePackageOrClass = ignorePackageOrClass;
        this.apiMapResultEnable = apiMapResultEnable;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return filter(returnType);
    }

    private boolean filter(MethodParameter returnType) {
        Class<?> declaringClass = returnType.getDeclaringClass();
        // 检查过滤包路径
        long count = ignorePackageOrClass.stream()
                .filter(declaringClass.getName()::contains)
                .count();
        if (count > 0) {
            return false;
        }
        // 检查<类>过滤列表
        if (ignorePackageOrClass.contains(declaringClass.getName())) {
            return false;
        }
        // 检查注解是否存在
        if (declaringClass.isAnnotationPresent(NoApiResult.class)) {
            return false;
        }
        return returnType.getMethod() == null || !returnType.getMethod().isAnnotationPresent(NoApiResult.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body == null) {
            if (returnType.getParameterType().getName().equals("java.lang.String")) {
                return apiMapResultEnable ? JSON.toJSON(ApiMapResult.ok()).toString() : JSON.toJSON(ApiResult.ok()).toString();
            }
            return apiMapResultEnable ? ApiMapResult.ok() : ApiResult.ok();
        }

        if (body instanceof ApiResult<?> || body instanceof ApiMapResult<?>) {
            return body;
        }

        if (body instanceof String) {
            return apiMapResultEnable ? JSON.toJSON(ApiMapResult.ok()).toString() : JSON.toJSON(ApiResult.ok(body)).toString();
        }

        return apiMapResultEnable ? ApiMapResult.ok(body) : ApiResult.ok(body);
    }
}
