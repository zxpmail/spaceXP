package cn.piesat.framework.dynamic.datasource.init;

import cn.piesat.framework.dynamic.datasource.properties.DataSourceProperties;

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
    void beforeCreate(DataSourceProperties dataSourceProperties);

    /**
     * 数据源创建之后允许做一些事情
     */
    void afterCreate(DataSourceProperties dataSourceProperties);

    default int getOrder() {
        return 0;
    }
}
