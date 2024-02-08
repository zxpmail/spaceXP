package cn.piesat.tools.generator.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;



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
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 项目类型
     */
    private String type;

    /**
     * 组织机构ID
     */
    private String groupId;

    /**
     * 项目id
     */
    private String artifactId;

    /**
     * 版本
     */
    private String version;

    /**
     * 描述
     */
    private String description;

    /**
     * 作者
     */
    private String author;

    /**
     * 作者email
     */
    private String  email;

    /**
     * 端口
     */
    private Integer port =8080;

    /**
     * 是否是默认 0否 1是
     */
    private Integer isDefault;

    /**
     * 生成文档 1、springdoc 0、springfox
     */
    private Integer springDoc;

    /**
     * 组件版本 1 为3.0.0 、0为2.0.0
     */
    private Integer bootVersion;
}
