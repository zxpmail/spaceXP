package cn.piesat.framework.dynamic.datasource.init;

import cn.piesat.framework.dynamic.datasource.model.DataSourceEntity;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;


import javax.sql.DataSource;
import java.util.Properties;

/**
 * <p/>
 * {@code @description}:
 * <p/>
 * {@code @create}: 2025-01-09 16:54
 * {@code @author}: zhouxp
 */
public class AtomikosDataSourceInit implements DataSourceInit{
    @Override
    public DataSource afterCreate(DataSource dataSource,DataSourceEntity dataSourceEntity) {
        Properties prop = new Properties();
        prop.put("url",dataSourceEntity.getUrl());
        prop.put("username",dataSourceEntity.getUsername());
        prop.put("password",dataSourceEntity.getPassword());
        prop.put("driverClassName",dataSourceEntity.getDriverClassName());
        AtomikosDataSourceBean ds =  new AtomikosDataSourceBean();

        ds.setUniqueResourceName(dataSourceEntity.getKey());
        ds.setXaProperties(prop);
        ds.setXaDataSourceClassName(dataSourceEntity.getXaDataSourceClassName());
        return ds;
    }
}
