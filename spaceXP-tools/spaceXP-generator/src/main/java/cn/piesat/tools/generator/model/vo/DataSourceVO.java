package cn.piesat.tools.generator.model.vo;

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
 * {@code @description}  :数据源VO实体类
 * <p/>
 * <b>@create:</b> 2023/12/4 15:36.
 *
 * @author zhouxp
 */
@Data
public class DataSourceVO {
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
    @Length(max = 128 , message = "长度必须小于等于128" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String connUrl;
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
