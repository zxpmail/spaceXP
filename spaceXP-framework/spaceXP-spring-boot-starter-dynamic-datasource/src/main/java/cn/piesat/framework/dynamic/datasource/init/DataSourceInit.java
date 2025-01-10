package cn.piesat.framework.dynamic.datasource.init;

import cn.piesat.framework.dynamic.datasource.model.DataSourceEntity;

import javax.sql.DataSource;

/**
 * <p/>
 * {@code @description}: 数据源初始化接口类
 * <p/>
 * {@code @create}: 2025-01-09 13:28
 * {@code @author}: zhouxp
 */
public interface DataSourceInit {
    /**
     * 创建之前允许做一些事情，比如用户名密码进行解密
     */
    default void beforeCreate(DataSourceEntity dataSourceEntity) {

    }

    /**
     * 数据源创建之后允许做一些事情
     */
    default DataSource afterCreate(DataSource dataSource, DataSourceEntity dataSourceEntity) {
        return dataSource;
    }

    default int getOrder() {
        return 0;
    }
}
