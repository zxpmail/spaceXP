package cn.piesat.framework.web.core;

import cn.piesat.framework.common.annotation.NoApiResult;
import cn.piesat.framework.common.model.vo.ApiResult;

import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.PathMatcher;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;


import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * <p/>
 * {@code @description}  :统一处理返回值
 * <p/>
 * <b>@create:</b> 2023/9/26 13:21.
 *
 * @author zhouxp
 */
@RequiredArgsConstructor
public class ApiResponseHandlerMethodReturnValueHandler implements HandlerMethodReturnValueHandler {
    private final HandlerMethodReturnValueHandler returnValueHandler;

    private final List<String> ignoreUrls;
    PathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        if(ObjectUtils.isEmpty(returnType)){
            return false;
        }
        return returnValueHandler.supportsReturnType(returnType);
    }

    /**
     * 处理返回值不包含下列情况下进行ApiResult包装
     * 1、当实体类中含有NoApiResult时
     * 2、当实体类已经是ApiResult
     * 3、当请求url包含在ignoreUrls中
     */
    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        HttpServletRequest nativeRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        boolean ifHandleReturnValue = true;
        if (returnType.getContainingClass().getAnnotation((NoApiResult.class)) != null
                || Objects.requireNonNull(returnType.getMethod()).getAnnotation(NoApiResult.class) != null
                || returnValue instanceof ApiResult) {
            ifHandleReturnValue = false;
        } else {
            assert nativeRequest != null;
            String method = nativeRequest.getMethod();
            String servletPath = nativeRequest.getServletPath();
            String restfulPath = method + ":" + servletPath;
            if(!CollectionUtils.isEmpty(ignoreUrls)){
                for (String api : ignoreUrls) {
                    if (pathMatcher.match(api, restfulPath)) {
                        ifHandleReturnValue = false;
                        break;
                    }
                }
            }
        }
        this.returnValueHandler.handleReturnValue(ifHandleReturnValue ? ApiResult.ok(returnValue) : returnValue, returnType, mavContainer, webRequest);
    }
}
