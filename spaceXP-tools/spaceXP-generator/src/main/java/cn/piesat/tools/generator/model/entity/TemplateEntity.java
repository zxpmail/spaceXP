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
     * 当为1时必须生成，如:controller、service等文件  当为2包含1的所有文件
     */
    private Integer must = 1;

    /**
     * 产生文件方式 单模块 1 多模块 2
     */
    private Integer moduleType = 1;

    /**
     * 模版内容
     */
    private String content;
}
