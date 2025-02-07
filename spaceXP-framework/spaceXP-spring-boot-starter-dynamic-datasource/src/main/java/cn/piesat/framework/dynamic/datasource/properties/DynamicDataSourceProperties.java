package cn.piesat.framework.dynamic.datasource.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p/>
 * {@code @description}: 动态数据源配置类
 * <p/>
 * {@code @create}: 2025-02-07 9:21
 * {@code @author}: zhouxp
 */
@Data
@ConfigurationProperties(prefix = DynamicDataSourceProperties.PREFIX)
public class DynamicDataSourceProperties {
    public static final String PREFIX = "spring.datasource.dynamic";

    /**
     * 必须设置默认的库,默认master
     */
    private String primary = "master";

    //所有的数据源
    private Map<String, DataSourceProperty> dataSource = new LinkedHashMap<>();
}
