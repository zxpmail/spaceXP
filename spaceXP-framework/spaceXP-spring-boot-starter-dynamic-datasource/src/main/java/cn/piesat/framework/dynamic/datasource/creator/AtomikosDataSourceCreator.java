package cn.piesat.framework.dynamic.datasource.creator;

import cn.piesat.framework.dynamic.datasource.config.AtomikosConfig;
import cn.piesat.framework.dynamic.datasource.enums.XADataSourceEnum;
import cn.piesat.framework.dynamic.datasource.properties.DataSourceProperty;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;

import javax.sql.DataSource;

import java.util.Properties;

import static cn.piesat.framework.dynamic.datasource.constants.DataSourceConstant.ATOMIKOS_DATASOURCE;

/**
 * <p/>
 * {@code @description}:
 * <p/>
 * {@code @create}: 2025-02-07 16:55
 * {@code @author}: zhouxp
 */
public class AtomikosDataSourceCreator extends AbstractDataSourceCreator{
    @Override
    protected DataSource doCreateDataSource(DataSourceProperty dataSourceProperty) {
        AtomikosConfig config = dataSourceProperty.getAtomikos();
        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();

        xaDataSource.setXaDataSourceClassName(dataSourceProperty.getDriverClassName());

        Properties xaProperties = new Properties();
        xaProperties.setProperty("url", dataSourceProperty.getUrl());
        xaProperties.setProperty("user", dataSourceProperty.getUsername());
        xaProperties.setProperty("password", dataSourceProperty.getPassword());
        xaDataSource.setXaProperties(xaProperties);

        xaDataSource.setUniqueResourceName(dataSourceProperty.getPoolName());
        xaDataSource.setMinPoolSize(config.getMinPoolSize());
        xaDataSource.setMaxPoolSize(config.getMaxPoolSize());
        xaDataSource.setBorrowConnectionTimeout(config.getBorrowConnectionTimeout());
        xaDataSource.setReapTimeout(config.getReapTimeout());
        xaDataSource.setMaxIdleTime(config.getMaxIdleTime());
        xaDataSource.setTestQuery(config.getTestQuery());
        xaDataSource.setMaintenanceInterval(config.getMaintenanceInterval());
        xaDataSource.setDefaultIsolationLevel(config.getDefaultIsolationLevel());
        xaDataSource.setMaxLifetime(config.getMaxLifetime());
        return xaDataSource;
    }

    @Override
    public boolean support(DataSourceProperty dataSourceProperty) {
        Class<? extends DataSource> type = dataSourceProperty.getType();
        return (type == null || ATOMIKOS_DATASOURCE.equals(type.getName())) && XADataSourceEnum.contains(dataSourceProperty.getDriverClassName());
    }
}
