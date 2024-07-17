package cn.piesat.tools.generator.model.entity;

import lombok.Data;

/**
 * <p/>
 * {@code @description}: 项目信息
 * <p/>
 * {@code @create}: 2024-07-17 15:15
 * {@code @author}: zhouxp
 */
@Data
public class ProjectEntity {

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
     * 作者
     */
    private String author;

    /**
     * 作者email
     */
    private String  email;

}
