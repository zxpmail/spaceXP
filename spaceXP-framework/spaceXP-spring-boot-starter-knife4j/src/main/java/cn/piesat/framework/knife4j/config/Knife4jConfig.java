package cn.piesat.framework.knife4j.config;

import cn.piesat.framework.knife4j.core.SwaggerBeanPostProcessor;
import cn.piesat.framework.knife4j.properties.SwaggerProperties;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RestController;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author :zhouxp
 * @date 2022/9/14 14:53
 * @description :自动配置
 */
@Configuration(proxyBeanMethods = false)
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
@ConditionalOnClass({Docket.class, ApiInfoBuilder.class})
@EnableConfigurationProperties(SwaggerProperties.class)
@Lazy
public class Knife4jConfig {
    @Bean
    public SwaggerBeanPostProcessor swaggerBeanPostProcessor(){
        return new SwaggerBeanPostProcessor();
    }


    private ApiInfo apiInfo(SwaggerProperties properties) {
        return new ApiInfoBuilder()
                .description(properties.getDescription())
                .termsOfServiceUrl(properties.getUrl())
                .contact(new Contact(properties.getAuthor() , properties.getUrl() , properties.getEmail()))
                .version(properties.getVersion())
                .title(properties.getTitle())
                .build();
    }

    @Bean
    public Docket createRestApi(SwaggerProperties properties) {
        return new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo(properties))
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build();
    }


}
