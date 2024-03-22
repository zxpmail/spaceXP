package cn.piesat.framework.kafka.datasource.config;

import cn.piesat.framework.kafka.datasource.core.KafkaCreator;
import cn.piesat.framework.kafka.datasource.properties.KafkaConfigProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



/**
 * <p/>
 * {@code @description}  : kafka自动配置类
 * <p/>
 * <b>@create:</b> 2024-03-21 11:01.
 *
 * @author zhouxp
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({KafkaConfigProperties.class})
public class KafkaDatasourceConfiguration {
    @Bean
    public KafkaCreator kafkaCreator(KafkaConfigProperties kafkaConfigProperties){
        return new KafkaCreator(kafkaConfigProperties);
    }

}
