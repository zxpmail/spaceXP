package cn.piesat.framework.dynamic.datasource.provider;

import cn.piesat.framework.dynamic.datasource.creator.LoadDataSourceCreator;
import cn.piesat.framework.dynamic.datasource.properties.DataSourceProperty;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p/>
 * {@code @description}:抽象多数据源加载类
 * <p/>
 * {@code @create}: 2025-02-09 13:55
 * {@code @author}: zhouxp
 */
@Slf4j
public abstract class AbstractDynamicDataSourceProvider implements DynamicDataSourceProvider {
    @Setter(onMethod_ = @Autowired)
    private LoadDataSourceCreator loadDataSourceCreator;

    protected Map<String, DataSource> createDataSourceMap(Map<String, DataSourceProperty> propertyMap) {
        if (propertyMap == null || propertyMap.isEmpty()) {
            return new ConcurrentHashMap<>();
        }
        Map<String, DataSource> map = new ConcurrentHashMap<>();
        for (Map.Entry<String, DataSourceProperty> entry : propertyMap.entrySet()) {
            try {
                DataSource dataSource = loadDataSourceCreator.createDataSource(entry.getValue());
                if (dataSource != null) {
                    map.put(entry.getKey(), dataSource);
                }
            } catch (Exception e) {
                log.error("Failed to create DataSource for key: " + entry.getKey(), e);
            }
        }
        return map;
    }
}
