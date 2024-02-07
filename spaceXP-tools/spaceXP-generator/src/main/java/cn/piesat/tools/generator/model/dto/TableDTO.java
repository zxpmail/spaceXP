package cn.piesat.tools.generator.model.dto;


import cn.piesat.framework.common.annotation.validator.group.AddGroup;
import cn.piesat.framework.common.annotation.validator.group.UpdateGroup;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;




/**
 * <p/>
 * {@code @description}  :表DTO类
 * <p/>
 * <b>@create:</b> 2024/1/4 10:10.
 *
 * @author zhouxp
 */
@Data
public class TableDTO  {
    /**
     * 功能名
     */
    private String version;
    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull(message = "主键不能为空", groups = UpdateGroup.class)
    private Long id;
    /**
     * 项目ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long projectId;
    /**
     * 表名
     */
    @NotBlank(message = "表名不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 100 , message = "长度必须小于等于100" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String tableName;
    /**
     * 实体类名称
     */
    @NotBlank(message = "类名不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 100 , message = "长度必须小于等于100" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String className;
    /**
     * 表说明
     */
    @NotBlank(message = "表说明不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 200 , message = "长度必须小于等于200" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String tableComment;

    /**
     * 数据源ID
     */
    @NotNull(message = "数据源ID不能为空", groups = UpdateGroup.class)
    private Long datasourceId;

    /**
     * 数据源连接名称
     */
    @NotNull(message = "数据源连接名称不能为空", groups = UpdateGroup.class)
    private String connName;

    /**
     * 数据库类型
     */
    private String dbType;
    /**
     * 项目标识
     */
    @NotBlank(message = "项目标识不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 100 , message = "长度必须小于等于100" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String moduleName;
    /**
     * 项目包名
     */
    @NotBlank(message = "项目包名不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 100 , message = "长度必须小于等于100" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String packageName;

    /**
     * 功能名
     */
    @NotBlank(message = "功能名不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 100 , message = "长度必须小于等于100" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String functionName;

    /**
     * 作者
     */
    @NotBlank(message = "作者不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 100 , message = "长度必须小于等于100" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String author;
    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 100 , message = "长度必须小于等于100" ,groups ={AddGroup.class,UpdateGroup.class} )
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
     * 生成文档 1、springdoc 0、springfox
     */
    @NotNull(message = "生成文档不能为空",groups ={AddGroup.class,UpdateGroup.class})
    private Integer springDoc;
}
