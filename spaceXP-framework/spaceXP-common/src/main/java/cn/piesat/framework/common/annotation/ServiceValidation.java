package cn.piesat.framework.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p/>
 * {@code @description}: service 中验证JSR303
 * <p/>
 * {@code @create}: 2024-08-09 15:07
 * {@code @author}: zhouxp
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER,ElementType.METHOD}) //定义了注解声明在哪些元素之前
@Documented
public @interface ServiceValidation {
    String message() default "{javax.validation.constraints.NotNull.message}";
}
