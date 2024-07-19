package cn.piesat.tools.generator.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * <p/>
 * {@code @description}  :项目实体类
 * <p/>
 * <b>@create:</b> 2023/12/4 9:55.
 *
 * @author zhouxp
 */
@Data
@TableName("gen_project")
public class ProjectDO extends BaseDO{

    /**
     * 项目类型 1为单模块 0为多模块
     */
    private Integer type;

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

}
