package cn.piesat.framework.dynamic.datasource.creator;

import cn.piesat.framework.dynamic.datasource.properties.DataSourceProperty;

import javax.sql.DataSource;

/**
 * <p/>
 * {@code @description}: 数据源创建接口
 * <p/>
 * {@code @create}: 2025-02-07 11:03
 * {@code @author}: zhouxp
 */
public interface DataSourceCreator {
    /**
     * 通过属性创建数据源
     */
    DataSource createDataSource(DataSourceProperty dataSourceProperty);

    /**
     * 当前创建器是否支持根据此属性创建
     */
    boolean support(DataSourceProperty dataSourceProperty);
}
