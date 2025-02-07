package cn.piesat.framework.dynamic.datasource.properties;

import lombok.Data;

import javax.sql.DataSource;

/**
 * <p/>
 * {@code @description}: 数据源配置类
 * <p/>
 * {@code @create}: 2025-02-07 9:25
 * {@code @author}: zhouxp
 */
@Data
public class DataSourceProperty {

    private Class<? extends DataSource> type;
    /**
     * JDBC driver
     */
    private String driverClassName;
    /**
     * JDBC url 地址
     */
    private String url;
    /**
     * JDBC 用户名
     */
    private String username;
    /**
     * JDBC 密码
     */
    private String password;

    /**
     * 解密公匙(如果未设置默认使用全局的)
     */
    private String publicKey;
}
