package cn.piesat.framework.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p/>
 *
 * @author zhouxp
 * @description :获取登录用户信息
 * <p/>
 * <b>@create:</b> 2022/10/17 13:41.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.PARAMETER})
@Documented
public @interface LoginUser {
}
