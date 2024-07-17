package cn.piesat.tools.generator.config;

import cn.piesat.tools.generator.model.entity.ProjectEntity;
import cn.piesat.tools.generator.model.entity.TemplateEntity;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p/>
 * {@code @description}: 模板配置类
 * <p/>
 * {@code @create}: 2024-07-16 14:55
 * {@code @author}: zhouxp
 */
@ConfigurationProperties(prefix = "gen")
@Data
@Component
public final class TemplatesConfig {

    /**
     * 模板列表
     */
    private List<TemplateEntity> templates;

    /**
     * 项目信息
     */
    private ProjectEntity project;


}
