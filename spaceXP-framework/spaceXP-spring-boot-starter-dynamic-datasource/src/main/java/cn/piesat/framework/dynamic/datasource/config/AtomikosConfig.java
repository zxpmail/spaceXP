package cn.piesat.framework.dynamic.datasource.config;

import lombok.Data;

/**
 * <p/>
 * {@code @description}: Atomikos 配置
 * <p/>
 * {@code @create}: 2025-02-07 16:52
 * {@code @author}: zhouxp
 */
@Data
public class AtomikosConfig {

    private int minPoolSize = 1;

    private int maxPoolSize = 10;

    private int borrowConnectionTimeout = 30;

    private int reapTimeout = 0;

    private int maxIdleTime = 60;

    private String testQuery = "SELECT 1";

    private int maintenanceInterval = 60;

    private int defaultIsolationLevel = -1;

    private int maxLifetime = 60;
}
