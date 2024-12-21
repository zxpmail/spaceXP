package cn.piesat.framework.log.annotation;

import cn.piesat.framework.log.enums.MdcLogType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p/>
 * {@code @description}: mdc注解
 * <p/>
 * {@code @create}: 2024-12-21 15:27
 * {@code @author}: zhouxp
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD}) //定义了注解声明在哪些元素之前
@Documented
public @interface MdcLog {
    //操作类型
    MdcLogType op() default MdcLogType.OTHER;

    String appName() default "";

    //操作流程ID
    String flowId() default "";

    //操作流程类型
    String flowType() default "";

    //操作流程名称
    String flowName() default "";


}

