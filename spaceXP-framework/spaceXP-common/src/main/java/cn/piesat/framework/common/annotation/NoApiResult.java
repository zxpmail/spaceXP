package cn.piesat.framework.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p/>
 * {@code @description}  :加上此注解不包装返回值
 * <p/>
 * <b>@create:</b> 2023/2/24 19:01.
 *
 * @author zhouxp
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.PARAMETER,ElementType.TYPE})
@Documented
public @interface NoApiResult {

    /**
     * 是否进行全局异常处理封装
     * @return true:进行处理;  false:不进行异常处理
     */
    boolean value() default true;
}
