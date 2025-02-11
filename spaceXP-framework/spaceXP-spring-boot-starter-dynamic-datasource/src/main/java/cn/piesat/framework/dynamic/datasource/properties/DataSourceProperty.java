package cn.piesat.framework.dynamic.datasource.properties;

import cn.piesat.framework.dynamic.datasource.config.AtomikosConfig;
import cn.piesat.framework.dynamic.datasource.config.DruidConfig;
import cn.piesat.framework.dynamic.datasource.config.HikariCpConfig;
import com.zaxxer.hikari.HikariConfig;
import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

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

    /**
     * 数据源类型
     */
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
     * 连接池名称
     */
    private String poolName;
    /**
     * 用私钥进行解密
     */
    private String privateKey;

    /**
     * Druid参数配置
     */
    @NestedConfigurationProperty
    private DruidConfig druid;
    /**
     * HikariCp参数配置
     */
    @NestedConfigurationProperty
    private HikariCpConfig hikari =new HikariCpConfig();
    /**
     * Atomikos参数配置
     */
    @NestedConfigurationProperty
    private AtomikosConfig atomikos = new AtomikosConfig();
}
