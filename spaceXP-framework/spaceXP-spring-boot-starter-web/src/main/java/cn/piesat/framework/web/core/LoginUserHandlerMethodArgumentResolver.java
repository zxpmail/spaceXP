package cn.piesat.framework.web.core;


import cn.piesat.framework.common.annotation.LoginUser;
import cn.piesat.framework.common.constants.CommonConstants;
import cn.piesat.framework.common.model.dto.JwtUser;

import org.springframework.core.MethodParameter;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * <p/>
 *
 * @author zhouxp
 * {@code @description} :当Controller方法含有@LoginUser注解时候，
 * 类型为JwtUser user的方法参数，把request域中自动赋值到user参数中
 * <p/>
 * <b>@create:</b> 2022/10/17 13:36.
 */

public class LoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return  parameter.hasParameterAnnotation(LoginUser.class);
    }

    /**
     *从Header获取参数绑定到JwtUser
     */
    @Override
    public Object resolveArgument(@Nonnull MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Object result = null;
        //获取用户ID

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if(ObjectUtils.isEmpty(request)){
            return null;
        }
        String userId = request.getHeader(CommonConstants.USER_ID);
        if (Objects.nonNull(userId)) {
            //获取用户信息
            String deptTemp =request.getHeader(CommonConstants.DEPT_ID);
            String username =request.getHeader(CommonConstants.USERNAME);
            String tenantTemp =request.getHeader(CommonConstants.TENANT_ID);
            Long deptLong= Objects.nonNull(deptTemp)?Long.parseLong(deptTemp):0L;
            Long tenantLong= Objects.nonNull(tenantTemp)?Long.parseLong(tenantTemp):0L;
            String deptName= request.getHeader(CommonConstants.DEPT_NAME);

            result = new JwtUser(Long.parseLong(userId), deptLong, username,deptName,tenantLong);
        }
        return result;
    }
}
