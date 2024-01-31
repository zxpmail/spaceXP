package cn.piesat.framework.permission.data.core;


import cn.piesat.framework.common.constants.CommonConstants;
import cn.piesat.framework.permission.data.model.UserDataPermission;
import cn.piesat.framework.permission.data.utils.DataPermissionContextHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * <p/>
 * {@code @description}  :数据权限过滤器
 * <p/>
 * <b>@create:</b> 2023/9/6 13:29.
 *
 * @author zhouxp
 */
@ConditionalOnClass(Filter.class)
public class DataPermissionFilter extends OncePerRequestFilter {

    @Resource
    private UserDataPermissionService userDataPermissionService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        try {
            String userId = request.getParameter(CommonConstants.USER_ID);
            /**
             * 默认用户权限，为了防止非法有直接调用url设置默认用户
             */
            UserDataPermission userDataPermission = new UserDataPermission()
                    .setUserId(-9999L)
                    .setDataScope(5);
            if (!StringUtils.hasText(userId)) {
                userId = request.getHeader(CommonConstants.USER_ID);
            }
            if (StringUtils.hasText(userId)) {
                UserDataPermission dataPermission = userDataPermissionService.getDataPermission(Long.valueOf(userId));
                if(Objects.isNull(dataPermission)){
                    DataPermissionContextHolder.setUserDataPermission(userDataPermission);
                }else{
                    DataPermissionContextHolder.setUserDataPermission(dataPermission);
                }
            }else{
                DataPermissionContextHolder.setUserDataPermission(userDataPermission);
            }
            filterChain.doFilter(request, response);
        } finally {
            DataPermissionContextHolder.clear();
        }
    }
}
