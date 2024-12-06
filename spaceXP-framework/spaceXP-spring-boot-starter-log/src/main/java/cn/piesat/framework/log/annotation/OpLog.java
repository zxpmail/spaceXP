package cn.piesat.framework.log.annotation;




import cn.piesat.framework.common.model.enums.BusinessEnum;

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
public @interface OpLog {
    //操作类型
    BusinessEnum op() default BusinessEnum.OTHER ;
    //操作描述
    String description() default "";
    /**
     * 慢日志阈值
     * 当值小于 0 时，不进行慢日志统计。
     * 当值大于等于0时，当前值只要大于等于这个值，就进行统计。
     */
    long slowThresholdMills() default -1;
}
