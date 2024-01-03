package cn.piesat.framework.dynamic.datasource.core;


import cn.piesat.framework.dynamic.datasource.annotation.DS;
import cn.piesat.framework.dynamic.datasource.utils.DataSourceContextHolder;
import cn.piesat.framework.dynamic.datasource.utils.DataSourceUtils;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
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

        // 如果DataSourceContextHolder.getDataSource()有值表示上层传来的数据源放行
        if (StringUtils.hasText(DataSourceContextHolder.getDataSource())) {
            return point.proceed();
        }

        // 如果类上有DS注解 而方法上没有DS进行ds处理，方法上又DS就放行
        DS ds = getClassAnnotation(point, DS.class);
        if (!Objects.isNull(ds)) {
            Method targetMethod = point.getMethod();
            if ( !Objects.isNull(targetMethod.getAnnotation(DS.class))) {
                return point.proceed();
            }

            String dsName = DataSourceUtils.getDsName(point);
            String dsValue = ds.value();
            if (StringUtils.hasText(dsName)) {
                DataSourceContextHolder.setDataSource(dsName);
            } else if (StringUtils.hasText(dsValue)) {
                DataSourceContextHolder.setDataSource(dsValue);
            }
        } else {
            //默认数据源
            DataSourceContextHolder.setDataSource("__master");
        }
        try {
            return point.proceed();
        } finally {
            DataSourceContextHolder.removeDataSource();
        }
    }



    private <T extends Annotation> T getClassAnnotation(MethodInvocation joinPoint, Class<T> annotationType) {
        Class<?> targetClass = getTargetClass(joinPoint);
        return targetClass.getAnnotation(annotationType) ;
    }

    private Class<?> getTargetClass(MethodInvocation joinPoint) {
        return joinPoint.getMethod().getDeclaringClass();
    }
}
