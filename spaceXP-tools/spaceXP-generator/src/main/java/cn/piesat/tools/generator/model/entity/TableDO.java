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
     * 数据源名称
     */
    private String datasourceName;

    /**
     * 项目ID
     */
    private Long projectId;
    /**
     * 项目标识
     */
    private String artifactId;
}
