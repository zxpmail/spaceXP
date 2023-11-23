package cn.piesat.framework.feign.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p/>
 * {@code @description}  :为了兼容第三方服务并兼顾远程调用效率问题，在有返回值ApiResult方法或接口上增加HasApiResult
 * <p/>
 * <b>@create:</b> 2023/2/24 19:01.
 *
 * @author zhouxp
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.PARAMETER,ElementType.TYPE})
@Documented
public @interface HasApiResult {
}
