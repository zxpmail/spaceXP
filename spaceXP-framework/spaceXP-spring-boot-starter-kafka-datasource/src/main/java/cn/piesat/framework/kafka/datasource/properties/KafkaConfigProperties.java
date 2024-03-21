package cn.piesat.framework.kafka.datasource.properties;

import cn.piesat.framework.kafka.datasource.model.DataSourceEntity;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p/>
 * {@code @description}  : kafka配置类
 * <p/>
 * <b>@create:</b> 2024-03-21 10:40.
 *
 * @author zhouxp
 */
@Data
@ConfigurationProperties(prefix = "space.kafka")
public class KafkaConfigProperties {
    @NestedConfigurationProperty
    private Map<String, DataSourceEntity> server = new LinkedHashMap<>();
}
