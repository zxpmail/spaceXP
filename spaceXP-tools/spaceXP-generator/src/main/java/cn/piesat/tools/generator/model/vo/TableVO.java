package cn.piesat.tools.generator.model.vo;

import cn.piesat.framework.common.annotation.validator.group.UpdateGroup;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * <p/>
 * {@code @description}  :数据表实体类
 * <p/>
 * <b>@create:</b> 2023/12/4 9:21.
 *
 * @author zhouxp
 */
@Data
public class TableVO {
    /**
     * 功能名
     */
    private String version;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
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
     * 表前缀
     */
    private String tablePrefix;

    /**
     * 表单布局  1：一列   2：两列
     */
    private Integer formLayout;

    /**
     * 表字段
     */
    private List<TableFieldVO> tableFields;
}
