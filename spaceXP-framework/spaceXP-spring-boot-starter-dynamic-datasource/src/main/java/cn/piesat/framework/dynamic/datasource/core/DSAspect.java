package cn.piesat.framework.dynamic.datasource.core;

import cn.piesat.framework.dynamic.annotation.DS;
import cn.piesat.framework.dynamic.datasource.utils.DataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

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

    @Pointcut("@annotation(cn.piesat.framework.dynamic.annotation.DS)")
    public void dynamicDataSource(){}

    @Around("dynamicDataSource()")
    public Object datasourceAround(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature)point.getSignature();
        Method method = signature.getMethod();
        DS ds = method.getAnnotation(DS.class);
        if (Objects.nonNull(ds)){
            DataSourceContextHolder.setDataSource(ds.value());
        }
        try {
            return point.proceed();
        } finally {
            DataSourceContextHolder.removeDataSource();
        }
    }
}
