package cn.piesat.tools.generator.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p/>
 * {@code @description}  :数据表字段实体类
 * <p/>
 * <b>@create:</b> 2023/11/27 17:53.
 *
 * @author zhouxp
 */
@Data
@TableName("gen_table_field")
public class TableFieldEntity {
    @TableId
    private Long id;
    /**
     * 表ID
     */
    private Long tableId;
    /**
     * 字段名称
     */
    private String fieldName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 字段类型
     */
    private String fieldType;
    /**
     * 字段说明
     */
    private String fieldComment;
    /**
     * 属性名
     */
    private String attrName;
    /**
     * 属性类型
     */
    private String attrType;
    /**
     * 属性包名
     */
    private String packageName;
    /**
     * 自动填充
     */
    private String autoFill;
    /**
     * 主键 0：否  1：是
     */
    private boolean primaryPk;
}
