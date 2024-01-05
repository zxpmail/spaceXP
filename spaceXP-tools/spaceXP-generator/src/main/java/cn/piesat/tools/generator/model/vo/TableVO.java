package cn.piesat.tools.generator.model.vo;

import cn.piesat.framework.common.annotation.validator.group.UpdateGroup;
import cn.piesat.tools.generator.model.entity.TableFieldDO;
import com.baomidou.mybatisplus.annotation.TableField;
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

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
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
     * 字段列表
     */
    @TableField(exist = false)
    private List<FieldTypeVO> fieldList;

    /**
     * 项目ID
     */
    @NotNull(message = "项目ID不能为空", groups = UpdateGroup.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long projectId;
    /**
     * 项目标识
     */
    private String artifactId;

    /**
     * 功能名
     */
    private String functionName;

}
