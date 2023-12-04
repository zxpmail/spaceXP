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
 * <b>@create:</b> 2023/12/4 15:46.
 *
 * @author zhouxp
 */
@Data
public class DataSourceDTO {
    /**
     * ID
     */
    @NotNull(message = "主键id不能为空", groups = UpdateGroup.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 数据库类型
     */
    @NotBlank(message = "数据库类型不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 30 , message = "长度必须小于等于30" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String dbType;
    /**
     * ip地址
     */
    @NotBlank(message = "ip地址不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 30 , message = "长度必须小于等于30" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String ipAddr;
    /**
     * 端口
     */
    @NotNull(message = "端口不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 30 , message = "长度必须小于等于30" ,groups ={AddGroup.class,UpdateGroup.class} )
    private Integer port;
    /**
     * 数据库名称
     */
    @NotBlank(message = "数据库名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 30 , message = "长度必须小于等于30" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String databaseName;
    /**
     * 连接名
     */
    @NotBlank(message = "连接名不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 30 , message = "长度必须小于等于30" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String connName;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 30 , message = "长度必须小于等于30" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String username;
    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 30 , message = "长度必须小于等于30" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String password;
}
