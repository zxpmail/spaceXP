package cn.piesat.framework.knife4j.config;

import cn.piesat.framework.knife4j.properties.SwaggerProperties;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author :zhouxp
 * @date 2022/9/14 14:53
 * @description :自动配置
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({SwaggerProperties.class})
public class Knife4jConfig {

    @Value("${spring.application.name:test}")
    private String appName;
    @Bean
    public OpenAPI springOpenAPI(SwaggerProperties swaggerProperties) {
        return new OpenAPI()
                .info(new Info().title(swaggerProperties.getTitle())
                        .description(swaggerProperties.getDescription())
                        .version(swaggerProperties.getVersion())
                        .termsOfService(swaggerProperties.getUrl())
                        .contact(new Contact()
                                .name(swaggerProperties.getAuthor())
                                .email(swaggerProperties.getEmail())
                                .url(swaggerProperties.getUrl()))
                        .license(new License().name("Apache 2.0").url("https://springdoc.org"))
                )
                .externalDocs(new ExternalDocumentation()
                        .description("SpringDoc Wiki Documentation")
                        .url("https://springdoc.org/v2"));
    }

    @Bean
    public GroupedOpenApi publicApi(SwaggerProperties swaggerProperties) {
        return GroupedOpenApi.builder()
                .group(appName)
                .pathsToMatch(swaggerProperties.getPathsToMatch())//API路径，不是类路径
                .build();
    }
}
