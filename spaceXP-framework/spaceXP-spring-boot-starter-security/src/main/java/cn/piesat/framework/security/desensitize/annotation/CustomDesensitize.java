package cn.piesat.framework.security.desensitize.annotation;

import cn.piesat.framework.security.desensitize.serialize.CustomSensitiveJsonSerializer;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p/>
 * {@code @description}  :自定义数据脱密注解
 * <p/>
 * <b>@create:</b> 2023/5/5 10:35.
 *
 * @author zhouxp
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(using = CustomSensitiveJsonSerializer.class)
public @interface CustomDesensitize {

    /**
     * 开始位置（包含）
     */
    int start() default 0;

    /**
     * 结束位置（不包含）
     */
    int end() default 0;

    String symbol() default "*";
}
