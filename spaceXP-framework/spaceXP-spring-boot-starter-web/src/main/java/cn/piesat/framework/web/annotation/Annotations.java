package cn.piesat.framework.web.annotation;

import java.lang.annotation.*;

/**
 * <p/>
 * {@code @description}:
 * <p/>
 * {@code @create}: 2025-02-26 19:18:52
 * {@code @author}: zhouxp
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Annotations {
    ConditionalValidateField[] value();
}
