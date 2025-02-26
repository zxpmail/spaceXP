package cn.piesat.framework.web.core;

import cn.piesat.framework.web.annotation.ConditionalValidateField;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * <p/>
 * {@code @description}: 条件验证字段信息
 * <p/>
 * {@code @create}: 2025-02-26 16:30:02
 * {@code @author}: zhouxp
 */
@Data
@AllArgsConstructor
public class ConditionalValidateFieldInfo {
    /**
     * 字段名
     */
    private String fieldName;
    /**
     * 字段注解
     */
    private ConditionalValidateField conditionalValidateField;
}
