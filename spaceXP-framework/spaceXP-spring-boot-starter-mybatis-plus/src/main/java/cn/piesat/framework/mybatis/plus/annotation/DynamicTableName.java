package cn.piesat.framework.mybatis.plus.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p/>
 * {@code @description}  :操作日志注解
 * <p/>
 * <b>@create:</b> 2022/11/28 8:43.
 *
 * @author zhouxp
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD}) //定义了注解声明在哪些元素之前
@Documented
public @interface DynamicTableName {

}
