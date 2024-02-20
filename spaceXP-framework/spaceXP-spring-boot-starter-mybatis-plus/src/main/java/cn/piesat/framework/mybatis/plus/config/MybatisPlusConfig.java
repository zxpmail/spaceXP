package cn.piesat.framework.mybatis.plus.config;


import cn.piesat.framework.mybatis.plus.core.AutoFillMetaObjectHandler;
import cn.piesat.framework.mybatis.plus.core.DynamicTableNameAspect;
import cn.piesat.framework.mybatis.plus.core.DynamicTableNameHandler;
import cn.piesat.framework.mybatis.plus.core.MybatisPlusExceptionHandler;
import cn.piesat.framework.mybatis.plus.properties.DynamicTableNameProperties;
import cn.piesat.framework.mybatis.plus.properties.MybatisPlusConfigProperties;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <p>MybatisPlus配置类</p>
 * @author :zhouxp
 * {@code @date} 2022/9/28 8:54
 * {@code @description} :MybatisPlus配置类
 */
@EnableTransactionManagement
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({MybatisPlusConfigProperties.class ,DynamicTableNameProperties.class})
@RequiredArgsConstructor
@Primary
public class MybatisPlusConfig {
    private final MybatisPlusConfigProperties mybatisPlusConfigProperties;
    @Value("${spring.application.name:test}")
    private String module;

    private final DynamicTableNameProperties dynamicTableNameProperties;

    @ConditionalOnMissingBean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        if (dynamicTableNameProperties.getEnable()){
            DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
            dynamicTableNameInnerInterceptor.setTableNameHandler(
                    new DynamicTableNameHandler(dynamicTableNameProperties.getTablePrefix()));
            interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);
        }
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }
    @Bean
    @Primary
    public AutoFillMetaObjectHandler myMetaObjectHandler(){
        return new AutoFillMetaObjectHandler(mybatisPlusConfigProperties.getCreateTime(),
                mybatisPlusConfigProperties.getUpdateTime(),
                mybatisPlusConfigProperties.getCreateId(),
                mybatisPlusConfigProperties.getUpdateId(),
                mybatisPlusConfigProperties.getDeptId(),
                mybatisPlusConfigProperties.getTenantId()
        );

    }

    @Bean
    public MybatisPlusExceptionHandler dbExceptionHandler(){
        return new MybatisPlusExceptionHandler(module);
    }

    @Bean
    @ConditionalOnProperty(name = "space.table.enable")
    public DynamicTableNameAspect dynamicTableNameAspect(){
        return new DynamicTableNameAspect();
    }

}
