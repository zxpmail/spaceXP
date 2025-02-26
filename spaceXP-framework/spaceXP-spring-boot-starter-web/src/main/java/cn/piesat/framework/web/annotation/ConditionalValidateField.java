package cn.piesat.framework.web.annotation;

import java.lang.annotation.*;

/**
 * <p/>
 * {@code @description}: 验证字段条件注解
 * <p/>
 * {@code @create}: 2025-02-26 16:16:34
 * {@code @author}: zhouxp
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConditionalValidateField {

    /**
     * 关联字段
     **/
    String relationField();

    /**
     * 要执行的的校验动作
     **/
    int action();

    /**
     * 值
     * **/
    String ifValue() default "";

    /**
     * 信息
     **/
    String message() default "";
}
