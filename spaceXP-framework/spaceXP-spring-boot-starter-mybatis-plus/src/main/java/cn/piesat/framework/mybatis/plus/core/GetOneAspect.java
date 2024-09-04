package cn.piesat.framework.mybatis.plus.core;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;


/**
 * <p/>
 * {@code @description}:
 * <p/>
 * {@code @create}: 2024-09-04 11:15
 * {@code @author}: zhouxp
 */
@Aspect
@Slf4j
public class GetOneAspect {

    private final String limitSql;

    public GetOneAspect(String limitSql) {
        this.limitSql = limitSql;
    }

    @Around("execution(* com.baomidou.mybatisplus.core.mapper.*.selectOne(..))")
    public Object addGetOneParam(ProceedingJoinPoint joinPoint) throws Throwable {
        Wrapper<?> wrapper;
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] originalArgs = joinPoint.getArgs();
        try {
            wrapper = (Wrapper<?>) originalArgs[0];
            if (wrapper instanceof QueryWrapper) {
                ((QueryWrapper<?>) wrapper).last(limitSql);
            } else if (wrapper instanceof LambdaQueryWrapper) {
                ((LambdaQueryWrapper<?>) wrapper).last(limitSql);
            } else {
                wrapper = new LambdaQueryWrapper<>();
                ((LambdaQueryWrapper<?>) wrapper).last(limitSql);
            }
            originalArgs[0] = wrapper;
        } catch (Exception e) {
            log.error("params error:{}", limitSql, e);
        }
        return joinPoint.proceed(originalArgs);
    }
}
