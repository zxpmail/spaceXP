package cn.piesat.tools.generator.model.entity;

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
public class TableFieldDO extends BaseDO{

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
     * 排序类型 0不排序 1升序 2 降序
     */
    private Integer sortType;
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
     * 查询方式
     */
    private String queryType;
    /**
     * 查询表单类型
     */
    private String queryFormType;

    /**
     * VO 1 是 0 否
     */
    private Integer vo;

    /**
     * DTO 1 是 0 否
     */
    private Integer dto;

    /**
     * 列表可显示 1是 0否
     */
    private Integer gridList;
}
