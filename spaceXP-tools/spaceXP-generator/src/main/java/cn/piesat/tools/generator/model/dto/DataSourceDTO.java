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
 * {@code @description}  :数据源DTO
 * <p/>
 * <b>@create:</b> 2024/1/18 14:10.
 *
 * @author zhouxp
 */
@Data
public class DataSourceDTO {
    /**
     * id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull(message = "主键不能为空", groups = UpdateGroup.class)
    private Long id;
    /**
     * 驱动类
     */
    @NotBlank(message = "驱动类型不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 50 , message = "长度必须小于等于50" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String driverClassName;

    /**
     * 数据库信息ID
     */
    @NotNull(message = "数据库信息ID不能为空", groups = {UpdateGroup.class, AddGroup.class})
    private Long databaseId;
    /**
     * 数据库类型
     */
    @NotBlank(message = "数据库类型不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 50 , message = "长度必须小于等于50" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String dbType;

    /**
     * 连接名
     */
    @NotBlank(message = "数据库类型不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 50 , message = "长度必须小于等于50" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String connName;
    /**
     * URL
     */
    @NotBlank(message = "url不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 200 , message = "长度必须小于等于200" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String url;
    /**
     * 用户名
     */
    @NotBlank(message = "用户不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 50 , message = "长度必须小于等于50" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 50 , message = "长度必须小于等于50" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String password;

}
