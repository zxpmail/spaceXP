package cn.piesat.tools.generator.model.query;

import lombok.Data;

/**
 * <p/>
 * {@code @description}  :属性类型查询实体类
 * <p/>
 * <b>@create:</b> 2023/12/6 8:59.
 *
 * @author zhouxp
 */
@Data
public class FieldTypeQuery {
    /**
     * 字段类型
     */
    private String columnType;
    /**
     * 属性类型
     */
    private String attrType;
}
