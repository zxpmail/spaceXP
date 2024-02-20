package cn.piesat.framework.web.config;

import cn.piesat.framework.common.properties.CommonProperties;

import cn.piesat.framework.web.core.LoginUserHandlerMethodArgumentResolver;

import cn.piesat.framework.web.core.TimeCostBeanPostProcessor;
import cn.piesat.framework.web.core.UniformReturnValueAdvice;
import cn.piesat.framework.web.core.WebExceptionHandler;
import cn.piesat.framework.web.properties.WebProperties;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;


/**
 * <p/>
 * {@code @description}  : web自动配置类
 * <p/>
 * <b>@create:</b> 2023/9/26 16:01.
 *
 * @author zhouxp
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({WebProperties.class})
public class WebAutoConfiguration implements WebMvcConfigurer {

    @Value("${spring.application.name:test}")
    private String module;
    @Bean
    @ConditionalOnProperty(name = "space.web.cost-enable", havingValue = "true")
    public TimeCostBeanPostProcessor timeCostBeanPostProcessor(){
        return new TimeCostBeanPostProcessor();
    }
    @ConditionalOnProperty(name = "space.web.web-exception-enable", havingValue = "true",matchIfMissing = true)
    @Bean
    public WebExceptionHandler exceptionHandler(){
        return new WebExceptionHandler(module);
    }

    @ConditionalOnProperty(name = "space.web.return-value-enable", havingValue = "true",matchIfMissing = true)
    @Bean
    public UniformReturnValueAdvice returnValueBean(WebProperties webProperties, CommonProperties commonProperties){
        List<String> ignorePackageOrClass = webProperties.getIgnorePackageOrClass();
        if(CollectionUtils.isEmpty(ignorePackageOrClass)){
            ignorePackageOrClass=new ArrayList<>();
        }
        ignorePackageOrClass.add("org.springframework");
        ignorePackageOrClass.add("org.springdoc");
        ignorePackageOrClass.add("springfox.documentation");
        return new UniformReturnValueAdvice(ignorePackageOrClass,commonProperties.getApiMapResultEnable());
    }

    @ConditionalOnProperty(name = "space.web.login-user-enable", havingValue = "true",matchIfMissing = true)
    @Bean
    public LoginUserHandlerMethodArgumentResolver loginUserHandlerMethodArgumentResolver(){
        return new LoginUserHandlerMethodArgumentResolver();
    }
    @ConditionalOnProperty(name = "space.web.login-user-enable", havingValue = "true",matchIfMissing = true)
    @ConditionalOnBean(LoginUserHandlerMethodArgumentResolver.class)
    @Bean
    public WebMvcConfigArg webMvcConfigArg(LoginUserHandlerMethodArgumentResolver loginUserHandlerMethodArgumentResolver){
        return new WebMvcConfigArg(loginUserHandlerMethodArgumentResolver);
    }

    @Bean
    @ConditionalOnProperty(name = "space.web.date-formatter-enable", havingValue = "true")
    @ConditionalOnMissingBean
    public Jackson2ObjectMapperBuilder objectMapperBuilder(WebProperties webProperties) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern(webProperties.getDateTimePattern())
                .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
                .toFormatter();
        return new Jackson2ObjectMapperBuilder()
                .createXmlMapper(false)
                .indentOutput(true)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .serializers(
                        new LocalDateSerializer(DateTimeFormatter.ofPattern(webProperties.getDatePattern())),
                        new LocalDateTimeSerializer(formatter))
                .deserializers(
                        new LocalDateDeserializer(DateTimeFormatter.ofPattern(webProperties.getDatePattern())),
                        new LocalDateTimeDeserializer(formatter))
                .modules(
                        new JavaTimeModule(),
                        new ParameterNamesModule(),
                        new Jdk8Module());
    }

}
