package cn.piesat.tools.generator.model.entity;


import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;


/**
 * <p/>
 * {@code @description}  :数据表实体类
 * <p/>
 * <b>@create:</b> 2023/12/4 9:21.
 *
 * @author zhouxp
 */
@Data
@TableName("gen_table")
public class TableDO extends BaseDO{

    /**
     * 项目ID
     */
    private Long projectId;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 实体类名称
     */
    private String className;
    /**
     * 功能名
     */
    private String tableComment;

    /**
     * 数据源ID
     */
    private Long datasourceId;

    /**
     * 数据源连接名称
     */
    private String connName;

    /**
     * 数据库类型
     */
    private String dbType;

    /**
     * 项目标识
     */
    private String moduleName;
    /**
     * 项目包名
     */
    private String packageName;

    /**
     * 功能名
     */
    private String functionName;

    /**
     * 作者
     */
    private String author;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 功能名
     */
    private String version;

    /**
     * 表单布局  1：一列   2：两列
     */
    private Integer formLayout;

    /**
     * 生成文档 1、springdoc 0、springfox
     */
    private Integer springDoc;
}
