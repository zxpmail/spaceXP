package cn.piesat.framework.mybatis.plus.external.config;

import cn.piesat.framework.mybatis.plus.external.core.CustomIdGenerator;
import cn.piesat.framework.mybatis.plus.external.properties.MybatisPlusExternalProperties;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p/>
 * {@code @description}  :额外配置类
 * <p/>
 * <b>@create:</b> 2024-06-06 14:34.
 *
 * @author zhouxp
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({MybatisPlusExternalProperties.class })
public class MybatisPlusExternalConfig {

    @Bean
    public IdentifierGenerator idGenerator(MybatisPlusExternalProperties externalProperties) {
        return new CustomIdGenerator(externalProperties);
    }

}