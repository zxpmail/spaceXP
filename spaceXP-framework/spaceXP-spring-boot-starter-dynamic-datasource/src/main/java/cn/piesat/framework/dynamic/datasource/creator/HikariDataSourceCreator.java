package cn.piesat.framework.dynamic.datasource.creator;

import cn.piesat.framework.dynamic.datasource.constants.DataSourceConstant;
import cn.piesat.framework.dynamic.datasource.properties.DataSourceProperty;
import cn.piesat.framework.dynamic.datasource.utils.ClassField2PropertiesUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.Properties;

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
        Properties properties = ClassField2PropertiesUtils.toProperties( dataSourceProperty.getHikari());
        HikariConfig config;
        if(properties.size()==0){
            config = new  HikariConfig();
        }else{
            config = new  HikariConfig(properties);
        }
        config.setUsername(dataSourceProperty.getUsername());
        config.setPassword(dataSourceProperty.getPassword());
        config.setJdbcUrl(dataSourceProperty.getUrl());
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
