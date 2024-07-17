package cn.piesat.tools.generator.model.entity;

import lombok.Data;

/**
 * <p/>
 * {@code @description}: 模版类
 * <p/>
 * {@code @create}: 2024-07-17 11:29
 * {@code @author}: zhouxp
 */
@Data
public class TemplateEntity {
    /**
     * ID号
     */
    private Integer id;
    /**
     * 文件名称
     */
    private String name;
    /**
     * 生成文件路径
     */
    private String path;
    /**
     * 是否生成单一文件 1 是 0 否 如系统pom、启动文件为单一文件，controller、service等不是单一文件
     */
    private Integer only = 0;

    /**
     * 产生文件方式 单模块 1 多模块 0
     */
    private Integer single = 1;

    /**
     * 模版内容
     */
    private String content;
}
