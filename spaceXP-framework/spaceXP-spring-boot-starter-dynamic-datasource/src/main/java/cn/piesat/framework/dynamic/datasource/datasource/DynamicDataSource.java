package cn.piesat.framework.dynamic.datasource.datasource;

import cn.piesat.framework.dynamic.datasource.creator.LoadDataSourceCreator;
import cn.piesat.framework.dynamic.datasource.properties.DataSourceProperty;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Objects;


/**
 * <p/>
 * {@code @description}: 动态数据源
 * <p/>
 * {@code @create}: 2025-02-11 14:44
 * {@code @author}: zhouxp
 */
public class DynamicDataSource {

    @Setter(onMethod_ = @Autowired)
    private DynamicRoutingDataSource dynamicRoutingDataSource;

    @Setter(onMethod_ = @Autowired)
    private LoadDataSourceCreator loadDataSourceCreator;

    public DataSource test(DataSourceProperty dataSourceProperty) {
        return loadDataSourceCreator.createDataSource(dataSourceProperty);
    }

    public Boolean add(DataSourceProperty dataSourceProperty, String dsName) {
        DataSource ds = test(dataSourceProperty);
        return add(ds, dsName);
    }

    public Boolean add(DataSource dataSource, String dsName) {
        if (StringUtils.hasText(dsName)) {
            dynamicRoutingDataSource.addDataSource(dsName, dataSource);
            return true;
        }
        return false;
    }

    public void add(Map<String, DataSourceProperty> dataSources) {
        dataSources.forEach((key, value) -> add(value, key));
    }

    private Boolean existsDataSource(String key) {
        return Objects.nonNull(dynamicRoutingDataSource.getDataSource(key));
    }

    public void delete(String key) {
        dynamicRoutingDataSource.close(key);
    }
}
