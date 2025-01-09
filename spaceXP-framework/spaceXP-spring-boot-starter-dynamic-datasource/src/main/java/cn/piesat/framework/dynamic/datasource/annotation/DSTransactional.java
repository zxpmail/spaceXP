package cn.piesat.framework.dynamic.datasource.annotation;

import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p/>
 * {@code @description}: 多数据源事务注解
 * <p/>
 * {@code @create}: 2025-01-09 10:53
 * {@code @author}: zhouxp
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Transactional
public @interface DSTransactional {
}
