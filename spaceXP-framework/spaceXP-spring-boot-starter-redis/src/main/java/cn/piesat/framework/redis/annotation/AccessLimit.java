package cn.piesat.framework.redis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p/>
 * {@code @description}  : 限流注解
 * <p/>
 * <b>@create:</b> 2023/12/19 16:06.
 *
 * @author zhouxp
 */
@Inherited
@Documented
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessLimit {
    /**
     * 指定second 时间内 API请求次数
     */
    int maxCount() default 5;

    /**
     * 请求次数的指定时间范围  秒数(redis数据过期时间)
     */
    int second() default 60;
}
