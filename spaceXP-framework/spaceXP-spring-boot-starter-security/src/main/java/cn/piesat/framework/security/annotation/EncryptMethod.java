package cn.piesat.framework.security.annotation;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p/>
 * {@code @description}  : 对方法参数进行加密
 * <p/>
 * <b>@create:</b> 2022/12/9 21:18.
 *
 * @author zhouxp
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface EncryptMethod {
    /**
     * 加密参数位置，默认第一个参数
     */
    int argsPos() default 0;
}
