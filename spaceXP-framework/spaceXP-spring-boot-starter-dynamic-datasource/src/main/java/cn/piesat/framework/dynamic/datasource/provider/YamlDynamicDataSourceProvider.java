package cn.piesat.framework.dynamic.datasource.provider;

import cn.piesat.framework.dynamic.datasource.properties.DataSourceProperty;

import javax.sql.DataSource;
import java.util.Map;

/**
 * <p/>
 * {@code @description}: 从yaml文件读取数据源属性，进行加载
 * <p/>
 * {@code @create}: 2025-02-09 14:02
 * {@code @author}: zhouxp
 */
public class YamlDynamicDataSourceProvider extends AbstractDynamicDataSourceProvider{

    private final Map<String, DataSourceProperty> propertyMap;

    public YamlDynamicDataSourceProvider(Map<String, DataSourceProperty> propertyMap) {
        this.propertyMap = propertyMap;
    }
    @Override
    public Map<String, DataSource> loadDataSources() {
        return createDataSourceMap(this.propertyMap);
    }
}
