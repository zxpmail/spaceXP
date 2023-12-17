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
 * {@code @description}  :项目实体类
 * <p/>
 * <b>@create:</b> 2023/12/4 9:55.
 *
 * @author zhouxp
 */
@Data
public class ProjectVO {
    /**
     * id
     */
    @NotNull(message = "主键id不能为空", groups = UpdateGroup.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 项目类型
     */
    @NotBlank(message = "项目类型不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 50 , message = "长度必须小于等于50" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String type;

    /**
     * 组织机构ID
     */
    @NotBlank(message = "项目包名不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 50 , message = "长度必须小于等于50" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String groupId;

    /**
     * 项目id
     */
    @NotBlank(message = "项目标识不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 50 , message = "长度必须小于等于50" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String artifactId;

    /**
     * 版本
     */
    @NotBlank(message = "项目版本不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 50 , message = "长度必须小于等于50" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String version;

    /**
     * 描述
     */
    @Length(max = 200 , message = "长度必须小于等于200" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String description;

    /**
     * 作者
     */
    @NotBlank(message = "项目作者不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 50 , message = "长度必须小于等于50" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String author;

    /**
     * 作者email
     */
    @NotBlank(message = "项目作者Email不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 50 , message = "长度必须小于等于50" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String  email;

    /**
     * 端口
     */
    private Integer port;

}
