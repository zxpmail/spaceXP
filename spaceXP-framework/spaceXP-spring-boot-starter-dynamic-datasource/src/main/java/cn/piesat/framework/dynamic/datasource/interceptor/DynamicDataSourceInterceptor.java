package cn.piesat.framework.dynamic.datasource.interceptor;

import cn.piesat.framework.dynamic.datasource.model.DSEntity;
import cn.piesat.framework.dynamic.datasource.support.DataSourceClassResolver;
import cn.piesat.framework.dynamic.datasource.utils.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p/>
 * {@code @description}: 当在方法或者类级别切到@DS会拦截
 * <p/>
 * {@code @create}: 2025-02-09 17:48
 * {@code @author}: zhouxp
 */
@Slf4j
public class DynamicDataSourceInterceptor implements MethodInterceptor {
    private final List<DataSourceClassResolver> dataSourceClassResolvers;

    public DynamicDataSourceInterceptor(List<DataSourceClassResolver> dataSourceClassResolvers) {
        this.dataSourceClassResolvers = dataSourceClassResolvers;
    }

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
        Object[] arguments = invocation.getArguments();
        if (arguments.length > 0) {
            for (Object argument : invocation.getArguments()) {
                if (argument instanceof DSEntity) {
                    DSEntity ds = (DSEntity) argument;
                    String dsName = ds.getDsName();
                    if (StringUtils.hasText(dsName)) {
                        return dsName;
                    }
                }
            }
        }
        if (this.dataSourceClassResolvers.isEmpty()) {
            log.warn("No data source class resolvers configured");
        } else {
            for (DataSourceClassResolver resolver : this.dataSourceClassResolvers) {
                try {
                    String key = resolver.findKey(invocation.getMethod(), invocation.getThis());
                    if (StringUtils.hasText(key)) {
                        return key;
                    }
                } catch (Exception e) {
                    log.error("Error while finding data source key with resolver: " + resolver.getClass().getName(), e);
                }
            }
            log.warn("No data source key found for method: " + invocation.getMethod().getName());
        }
        return "";
    }
}
