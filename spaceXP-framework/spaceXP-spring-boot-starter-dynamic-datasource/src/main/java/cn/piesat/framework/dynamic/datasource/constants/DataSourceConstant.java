package cn.piesat.framework.dynamic.datasource.constants;

/**
 * <p/>
 * {@code @description}:
 * <p/>
 * {@code @create}: 2025-02-07 13:31
 * {@code @author}: zhouxp
 */
public interface DataSourceConstant {
    /**
     * 数据源：主库
     */
    String MASTER = "master";
    /**
     * 数据源：从库
     */
    String SLAVE = "slave";
    /**
     * DRUID数据源类
     */
    String DRUID_DATASOURCE = "com.alibaba.druid.pool.DruidDataSource";
    /**
     * HikariCp数据源
     */
    String HIKARI_DATASOURCE = "com.zaxxer.hikari.HikariDataSource";

    /**
     * Atomikos数据源
     */
    String ATOMIKOS_DATASOURCE = "com.atomikos.jdbc.AtomikosDataSourceBean";

}
