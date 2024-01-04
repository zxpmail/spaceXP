package cn.piesat.framework.dynamic.datasource.config;

import cn.piesat.framework.dynamic.datasource.core.DSAspect;
import cn.piesat.framework.dynamic.datasource.core.DynamicDataSource;
import cn.piesat.framework.dynamic.datasource.core.DynamicMethodInterceptor;
import cn.piesat.framework.dynamic.datasource.model.DataSourceEntity;
import cn.piesat.framework.dynamic.datasource.properties.DataSourceProperties;
import cn.piesat.framework.dynamic.datasource.utils.DataSourceUtils;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * {@code @description}  : 数据源配置类
 * <p/>
 * <b>@create:</b> 2023/12/9 14:48.
 *
 * @author zhouxp
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({DataSourceProperties.class})
public class DataSourceConfiguration {
    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSource masterDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "dynamicDataSource")
    @Primary
    public DynamicDataSource createDynamicDataSource(DataSource masterDataSource,DataSourceProperties dataSourceProperties){
        Map<Object,Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("__master",masterDataSource);
        if(!CollectionUtils.isEmpty(dataSourceProperties.getDss())){
            for (Map.Entry<String, DataSourceEntity> entry : dataSourceProperties.getDss().entrySet()) {
                DataSource test = DataSourceUtils.test(entry.getValue());
                dataSourceMap.put(entry.getKey(),test);
            }

        }
        return new DynamicDataSource(masterDataSource,dataSourceMap);
    }
    @Bean
    public DSAspect dSAspect(){
        return new DSAspect();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.pointcut")
    public AspectJExpressionPointcutAdvisor configurableAdvisor(DataSourceProperties dataSourceProperties) {
        if(StringUtils.hasText(dataSourceProperties.getPointcut())) {
            AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
            advisor.setExpression(dataSourceProperties.getPointcut());
            advisor.setAdvice(new DynamicMethodInterceptor());
            return advisor;
        }
        return null;
    }

}
