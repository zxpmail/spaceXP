package cn.piesat.framework.dynamic.datasource.core;


import cn.piesat.framework.dynamic.datasource.annotation.DS;
import cn.piesat.framework.dynamic.datasource.utils.DataSourceContextHolder;
import cn.piesat.framework.dynamic.datasource.utils.DataSourceUtils;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Objects;


/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2024/1/3 9:35.
 *
 * @author zhouxp
 */
public class DynamicMethodInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation point) throws Throwable {

        // 如果类上有DS注解 而方法上没有DS进行ds处理，方法上又DS就放行
        String dsName = DataSourceUtils.getDsName(point);
        if (StringUtils.hasText(dsName)) {
            DataSourceContextHolder.push(dsName);
        } else {
            DS ds = getClassAnnotation(point);
            if (ds == null) {
                Method targetMethod = point.getMethod();
                ds = targetMethod.getAnnotation(DS.class);
                if (Objects.isNull(ds)) {
                    return point.proceed();
                }
            }
            String dsValue = ds.value();
            if (StringUtils.hasText(dsValue)) {
                DataSourceContextHolder.push(dsValue);
            } else {
                //默认数据源
                DataSourceContextHolder.push("__master");
            }
        }
        try {
            return point.proceed();
        } finally {
            DataSourceContextHolder.poll();
        }
    }



    private DS getClassAnnotation(MethodInvocation joinPoint) {
        Class<?> targetClass = getTargetClass(joinPoint);
        return targetClass.getAnnotation(DS.class);
    }

    private Class<?> getTargetClass(MethodInvocation joinPoint) {
        return joinPoint.getMethod().getDeclaringClass();
    }
}
