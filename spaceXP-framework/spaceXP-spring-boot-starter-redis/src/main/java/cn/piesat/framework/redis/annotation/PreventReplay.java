package cn.piesat.framework.redis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p/>
 * {@code @description}  : 接口防刷
 *                         在相应需要防刷的方法上加上
 * <p/>
 * <b>@create:</b> 2023/12/19 15:03.
 *
 * @author zhouxp
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PreventReplay {
    /**
     * 限制的时间值（秒）
     */
    int value() default 60;

    /**
     * 提示
     */
    String message() default "";
}
