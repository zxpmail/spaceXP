package cn.piesat.tools.generator.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p/>
 * {@code @description}: 以字母开始
 * <p/>
 * {@code @create}: 2024-07-19 15:22
 * {@code @author}: zhouxp
 */
@Documented
@Constraint(validatedBy = StartsWithCharValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StartsWithChar {
    String message() default "{invalid.start.char}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}