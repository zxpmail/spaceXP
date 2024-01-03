package cn.piesat.framework.dynamic.datasource.core;

import cn.piesat.framework.dynamic.datasource.annotation.DS;
import cn.piesat.framework.dynamic.datasource.utils.DataSourceContextHolder;
import cn.piesat.framework.dynamic.datasource.utils.DataSourceUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2023/12/9 14:41.
 *
 * @author zhouxp
 */
@Aspect
@Slf4j
public class DSAspect {

    @Pointcut("@annotation(cn.piesat.framework.dynamic.datasource.annotation.DS)")
    public void dynamicDataSource(){}

    @Around("dynamicDataSource()")
    public Object datasourceAround(ProceedingJoinPoint point) throws Throwable {
        String dsName = DataSourceUtils.getDsName(point);
        if(!StringUtils.hasText(dsName)) {
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();
            DS ds = method.getAnnotation(DS.class);
            if (Objects.nonNull(ds)) {
                DataSourceContextHolder.push(ds.value());
            } else {
                DataSourceContextHolder.push("__master");
            }
        }
        try {
            return point.proceed();
        } finally {
            DataSourceContextHolder.poll();
        }
    }
}
