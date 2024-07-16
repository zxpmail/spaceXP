package cn.piesat.tools.generator.config;

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
@ConfigurationProperties(prefix = "gen" )
@Data
@Component
public class TemplatesConfig {

    /**
     * 模板列表
     */
    private List<Templates> templates;

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
        private String packageName = "cn.piesat";
        private String version = "1.0.0";
    }

    /**
     * 开发者实现信息类
     */
    @Data
    static class Developer {
        private String author = "zhouxiaoping";
        private String email = "zhouxiaoping@piesat.cn";
    }

    /**
     * 模板信息实体类
     */
    @Data
    static class Templates {
        private String name;
        private String path;
    }
}
