package cn.piesat.framework.dynamic.datasource.interceptor;

import cn.piesat.framework.dynamic.datasource.utils.DynamicDataSourceContextHolder;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * <p/>
 * {@code @description}: 当在方法或者类级别切到@DS会拦截
 * <p/>
 * {@code @create}: 2025-02-09 17:48
 * {@code @author}: zhouxp
 */
public class DynamicDataSourceInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String dsKey = determineDataSourceKey(invocation);
        try {
            DynamicDataSourceContextHolder.addDataSource(dsKey);
            return invocation.proceed();
        } finally {
            DynamicDataSourceContextHolder.removeCurrentDataSource();
        }
    }

    private String determineDataSourceKey(MethodInvocation invocation) {
        return "";
    }
}
