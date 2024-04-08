package cn.piesat.tools.generator.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Objects;

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

public class TableFieldDO extends BaseDO{
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TableFieldDO that = (TableFieldDO) o;
        return super.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), super.getId());
    }

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
     * 字段是否可重复 0不重复 1重复
     */
    private Integer fieldRepeat;
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
     * 自动填充 DEFAULT、INSERT、UPDATE、INSERT_UPDATE
     */
    private String autoFill;
    /**
     * 主键 0：否  1：是
     */
    private Integer primaryPk;

    /**
     * 表单项 0：否  1：是
     */
    private Integer formItem;
    /**
     * 表单必填 0：否  1：是
     */
    private Integer formRequired;
    /**
     * 表单类型
     */
    private String formType;
    /**
     * 表单字典类型
     */
    private String formDict;
    /**
     * 表单效验
     */
    private String formValidator;
    /**
     * 列表项 0：否  1：是
     */
    private Integer gridItem;
    /**
     * 列表排序 0：否  1：是
     */
    private Integer gridSort;
    /**
     * 查询项 0：否  1：是
     */
    private Integer queryItem;

    /**
     * 0不排序 1升序 2 降序
     */
    private Integer sortType;

    /**
     * DTO 1是0否
     */
    private Integer dto;

    /**
     * VO 1是 0否
     */
    private Integer vo;

    /**
     * 列表可显示 1是 0否
     */
    private Integer gridList;

    /**
     * 查询方式
     */
    private String queryType;
    /**
     * 查询表单类型
     */
    private String queryFormType;

    /**
     * 字段长度，为进行JSR303进行字符串验证，此字段只对字符串字段有用
     */
    private Integer len;

}
