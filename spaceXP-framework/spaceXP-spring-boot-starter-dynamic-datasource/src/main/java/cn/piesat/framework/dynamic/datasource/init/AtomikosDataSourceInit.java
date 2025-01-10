package cn.piesat.framework.dynamic.datasource.init;

import cn.piesat.framework.dynamic.datasource.model.DataSourceEntity;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.util.StringUtils;


import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * <p/>
 * {@code @description}:
 * <p/>
 * {@code @create}: 2025-01-09 16:54
 * {@code @author}: zhouxp
 */
public class AtomikosDataSourceInit implements DataSourceInit {
    @Override
    public DataSource afterCreate(DataSource dataSource, DataSourceEntity dataSourceEntity) {

        if (dataSourceEntity == null) {
            throw new IllegalArgumentException("dataSourceEntity cannot be null");
        }
        if (StringUtils.hasText(dataSourceEntity.getUrl()) && StringUtils.hasText(dataSourceEntity.getUsername()) &&
                StringUtils.hasText(dataSourceEntity.getPassword()) && StringUtils.hasText(dataSourceEntity.getDriverClassName()) &&
                StringUtils.hasText(dataSourceEntity.getKey()) && StringUtils.hasText(dataSourceEntity.getXaDataSourceClassName())) {
            throw new IllegalArgumentException("One or more required fields in dataSourceEntity are null");
        }
        Properties prop = new Properties();
        prop.put("url", dataSourceEntity.getUrl());
        prop.put("username", dataSourceEntity.getUsername());
        prop.put("password", dataSourceEntity.getPassword());
        prop.put("driverClassName", dataSourceEntity.getDriverClassName());

        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        try {
            ds.setUniqueResourceName(dataSourceEntity.getKey());
            ds.setXaProperties(prop);
            ds.setXaDataSourceClassName(dataSourceEntity.getXaDataSourceClassName());
            ds.init();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize AtomikosDataSourceBean: " + e.getMessage(), e);
        }
        try {
            dataSource.getConnection().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ds;
    }
}
