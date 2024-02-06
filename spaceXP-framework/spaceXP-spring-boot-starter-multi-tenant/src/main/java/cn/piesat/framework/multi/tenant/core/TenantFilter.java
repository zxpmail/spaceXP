package cn.piesat.framework.multi.tenant.core;


import cn.piesat.framework.common.constants.CommonConstants;
import cn.piesat.framework.common.model.dto.TwoDTO;

import cn.piesat.framework.multi.tenant.utils.TenantContextHolder;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

/**
 * <p/>
 * {@code @description}  : 拦截request域，把多租标志放入线程上下文中
 * <p/>
 * <b>@create:</b> 2023/9/5 15:22.
 *
 * @author zhouxp
 */
@ConditionalOnClass(Filter.class)
public class TenantFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            //优先获取请求参数中的tenantId值
            String tenantId = request.getHeader(CommonConstants.TENANT_ID);
            String userName=request.getHeader(CommonConstants.USERNAME);
            TwoDTO<Long,String> tenant =null;
            //保存租户id
            if (StringUtils.hasText(tenantId)) {
                tenant =new TwoDTO<>();
                tenant.setFirst(Long.valueOf(tenantId));
            }
            if (StringUtils.hasText(userName)) {
                if(ObjectUtils.isEmpty(tenant)){
                    tenant =new TwoDTO<>();
                }
                tenant.setSecond(userName);
            }
            if(!ObjectUtils.isEmpty(tenant)){
                TenantContextHolder.setTenant(tenant);
            }
            filterChain.doFilter(request, response);
        } finally {
            TenantContextHolder.clear();
        }
    }
}