package cn.piesat.tools.generator.model;

import lombok.Data;

/**
 * <p/>
 * {@code @description}  : 模板信息
 * <p/>
 * <b>@create:</b> 2023/12/26 17:51.
 *
 * @author zhouxp
 */
@Data
public class TemplateInfo {
    /**
     * 模板名称
     */
    private String templateName;
    /**
     * 模板内容
     */
    private String templateContent;

    private String generatorPath;
}
