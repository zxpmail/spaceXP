package cn.piesat.tools.generator.model.dto;

import cn.piesat.framework.common.annotation.validator.group.AddGroup;
import cn.piesat.framework.common.annotation.validator.group.UpdateGroup;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p/>
 * {@code @description}  :导入数据源DTO
 * <p/>
 * <b>@create:</b> 2024/1/18 14:10.
 *
 * @author zhouxp
 */
@Data
public class ImportDataSourceDTO {
    /**
     * id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull(message = "主键不能为空", groups = UpdateGroup.class)
    private Long id;

    /**
     * 数据库信息ID
     */
    @NotNull(message = "数据库信息ID不能为空", groups = {UpdateGroup.class, AddGroup.class})
    @JsonSerialize(using = ToStringSerializer.class)
    private Long databaseId;

    /**
     * 数据库连接
     */
    @NotBlank(message = "数据库连接不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 50 , message = "长度必须小于等于50" ,groups ={UpdateGroup.class} )
    private String connName;

    /**
     * 数据库类型
     */
    @NotBlank(message = "数据库类型不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 50 , message = "长度必须小于等于50" ,groups ={UpdateGroup.class} )
    private String dbType;

    /**
     * 数据库类型
     */
    @NotBlank(message = "数据库名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 50 , message = "长度必须小于等于50" ,groups ={UpdateGroup.class} )
    private String databaseName;

}
