package cn.piesat.framework.web.annotation;

import java.lang.annotation.*;

/**
 * <p/>
 * {@code @description}: 条件校验注解
 * <p/>
 * {@code @create}: 2025-02-26 17:25:46
 * {@code @author}: zhouxp
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConditionalValidate {
}
