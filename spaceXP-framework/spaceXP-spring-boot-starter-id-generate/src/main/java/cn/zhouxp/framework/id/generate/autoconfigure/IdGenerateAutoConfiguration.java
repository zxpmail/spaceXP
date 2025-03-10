package cn.zhouxp.framework.id.generate.autoconfigure;

import cn.zhouxp.framework.id.generate.properties.IdGenerateProperties;
import cn.zhouxp.framework.id.generate.service.IdGenerateService;
import cn.zhouxp.framework.id.generate.service.impl.IdGenerateServiceImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2025-03-10 09:11:12
 *
 * @author zhouxp
 */
@Configuration
@EnableConfigurationProperties(value = IdGenerateProperties.class)
public class IdGenerateAutoConfiguration {
    @Bean
    public IdGenerateService idGenerateService(IdGenerateProperties idGenerateProperties) {
        return new IdGenerateServiceImpl(idGenerateProperties);
    }
}
