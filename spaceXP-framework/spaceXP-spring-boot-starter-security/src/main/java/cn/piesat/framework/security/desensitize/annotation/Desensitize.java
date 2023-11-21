package cn.piesat.framework.security.desensitize.annotation;

import cn.piesat.framework.security.desensitize.enums.DesensitizeRuleEnums;

import cn.piesat.framework.security.desensitize.serialize.SensitiveJsonSerializer;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p/>
 * {@code @description}  :数据脱敏注解
 * <p/>
 * <b>@create:</b> 2023/5/4 17:00.
 *
 * @author zhouxp
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveJsonSerializer.class)
public @interface Desensitize {

    /**
     * 脱敏规则
     */
    DesensitizeRuleEnums rule() default DesensitizeRuleEnums.USER_ID;

}
