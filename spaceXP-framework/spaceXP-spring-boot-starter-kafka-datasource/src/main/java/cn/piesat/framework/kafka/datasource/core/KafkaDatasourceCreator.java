package cn.piesat.framework.kafka.datasource.core;

import cn.piesat.framework.kafka.datasource.properties.KafkaConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

/**
 * <p/>
 * {@code @description}  : kafka数据源创建器
 * <p/>
 * <b>@create:</b> 2024-03-21 10:53.
 *
 * @author zhouxp
 */
@Slf4j
public class KafkaDatasourceCreator implements InitializingBean {
    private final KafkaConfigProperties kafkaConfigProperties ;

    public KafkaDatasourceCreator(KafkaConfigProperties kafkaConfigProperties) {
        this.kafkaConfigProperties = kafkaConfigProperties;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
