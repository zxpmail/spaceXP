package cn.piesat.framework.dynamic.datasource.creator;

import cn.piesat.framework.dynamic.datasource.config.DruidConfig;
import cn.piesat.framework.dynamic.datasource.constants.DataSourceConstant;
import cn.piesat.framework.dynamic.datasource.properties.DataSourceProperty;
import cn.piesat.framework.dynamic.datasource.utils.ClassField2PropertiesUtils;
import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;
import java.util.Objects;

/**
 * <p/>
 * {@code @description}: Druid数据源创建类
 * <p/>
 * {@code @create}: 2025-02-07 15:48
 * {@code @author}: zhouxp
 */
public class DruidDataSourceCreator extends AbstractDataSourceCreator{
    @Override
    protected DataSource doCreateDataSource(DataSourceProperty dataSourceProperty) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(dataSourceProperty.getUsername());
        dataSource.setPassword(dataSourceProperty.getPassword());
        dataSource.setUrl(dataSourceProperty.getUrl());
        dataSource.setDriverClassName(dataSourceProperty.getDriverClassName());
        DruidConfig druidConfig = dataSourceProperty.getDruid();
        if (Objects.nonNull(druidConfig)) {
            dataSource.configFromPropeties(ClassField2PropertiesUtils.toProperties(druidConfig));

        }
        try {
            dataSource.init();
        } catch (Exception e) {
            throw new RuntimeException("druid datasource create error, e=", e);
        }
        return dataSource;
    }

    @Override
    public boolean support(DataSourceProperty dataSourceProperty) {
        Class<? extends DataSource> type = dataSourceProperty.getType();
        return Objects.nonNull(type) && Objects.equals(type.getName(), DataSourceConstant.DRUID_DATASOURCE);
    }
}
