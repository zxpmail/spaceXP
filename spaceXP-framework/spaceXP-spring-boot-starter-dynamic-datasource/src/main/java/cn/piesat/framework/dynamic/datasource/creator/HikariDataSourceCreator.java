package cn.piesat.framework.dynamic.datasource.creator;

import cn.piesat.framework.dynamic.datasource.constants.DataSourceConstant;
import cn.piesat.framework.dynamic.datasource.properties.DataSourceProperty;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

/**
 * <p/>
 * {@code @description}:HikariCP数据源创建类
 * <p/>
 * {@code @create}: 2025-02-07 13:25
 * {@code @author}: zhouxp
 */
public class HikariDataSourceCreator extends AbstractDataSourceCreator{
    @Override
    protected DataSource doCreateDataSource(DataSourceProperty dataSourceProperty) {
        HikariConfig config = dataSourceProperty.getHikari();
        config.setUsername(dataSourceProperty.getUsername());
        config.setPassword(dataSourceProperty.getPassword());
        config.setJdbcUrl(dataSourceProperty.getUrl());
        config.setPoolName(dataSourceProperty.getPoolName());
        String driverClassName = dataSourceProperty.getDriverClassName();
        if (StringUtils.hasText(driverClassName)) {
            config.setDriverClassName(driverClassName);
        }
        return new HikariDataSource(config);
    }

    @Override
    public boolean support(DataSourceProperty dataSourceProperty) {
        Class<? extends DataSource> type = dataSourceProperty.getType();
        return type == null || DataSourceConstant.HIKARI_DATASOURCE.equals(type.getName());
    }
}
