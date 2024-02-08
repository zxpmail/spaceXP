package cn.piesat.tools.generator.model.dto;

import cn.piesat.framework.common.annotation.validator.group.AddGroup;
import cn.piesat.framework.common.annotation.validator.group.UpdateGroup;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


import java.util.List;

/**
 * <p/>
 * {@code @description}  :项目DTO
 * <p/>
 * <b>@create:</b> 2024/1/22 15:34.
 *
 * @author zhouxp
 */
@Data
public class ProjectDTO {
    /**
     * id
     */
    @NotNull(message = "主键id不能为空", groups = UpdateGroup.class)
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
    private Integer port=8080;

    /**
     * 是否是默认 0否 1是
     */
    @NotNull(message = "是否是默认", groups =  {AddGroup.class, UpdateGroup.class})
    private Integer isDefault;

    /**
     * 生成文档 1、springdoc 0、springfox
     */
    @NotNull(message = "生成文档", groups =  {AddGroup.class, UpdateGroup.class})
    private Integer springDoc;


    /**
     * 组件版本 1 为3.0.0 、0为2.0.0
     */
    @NotNull(message = "组件版本", groups =  {AddGroup.class, UpdateGroup.class})
    private Integer bootVersion;

    /**
     * 表前缀
     */
    private String tablePrefix;

    private List<TableDTO> tables;


}
