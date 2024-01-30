package cn.piesat.tools.generator.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * <p/>
 * {@code @description}  :字段类型实体类
 * <p/>
 * <b>@create:</b> 2023/12/4 13:16.
 *
 * @author zhouxp
 */
@Data
@TableName("gen_field_type")
public class FieldTypeDO extends BaseDO{

    /**
     * 字段类型
     */
    private String columnType;
    /**
     * 属性类型
     */
    private String attrType;
    /**
     * 属性包名
     */
    private String packageName;

}
