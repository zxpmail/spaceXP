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
@ConfigurationProperties(prefix = "gen")
@Data
@Component
public final class TemplatesConfig {

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

    /**
     * 模板信息实体类
     */
    @Data
    static class Templates {
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
    }
}
