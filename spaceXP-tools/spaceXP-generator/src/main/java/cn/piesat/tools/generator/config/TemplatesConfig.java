package cn.piesat.tools.generator.config;

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
    private Project project;

    /**
     * 开发者
     */
    private Developer developer;

    /**
     * 项目信息实体类
     */
    @Data
    static class Project {
        /**
         * 项目包
         */
        private String packageName = "cn.piesat";
        /**
         * 项目版本
         */
        private String version = "1.0.0";
    }

    /**
     * 开发者实现信息类
     */
    @Data
    static class Developer {
        /**
         * 创造者
         */
        private String author = "zhouxiaoping";
        /**
         * 创建者EMail
         */
        private String email = "zhouxiaoping@piesat.cn";
    }

}
