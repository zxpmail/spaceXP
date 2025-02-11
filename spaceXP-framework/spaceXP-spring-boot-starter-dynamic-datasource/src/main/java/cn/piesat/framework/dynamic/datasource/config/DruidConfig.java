package cn.piesat.framework.dynamic.datasource.config;

import lombok.Data;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.Properties;

/**
 * <p/>
 * {@code @description}: Druid配置类
 * <p/>
 * {@code @create}: 2025-02-07 9:39
 * {@code @author}: zhouxp
 */
@Data
public class DruidConfig {
    private long connectCount;
    private long closeCount;
    private volatile long connectErrorCount;
    private long recycleCount;
    private long removeAbandonedCount;
    private long notEmptyWaitCount;
    private long notEmptySignalCount;
    private long notEmptyWaitNanos;
    private int keepAliveCheckCount;
    private int activePeak;
    private long activePeakTime;
    private int poolingPeak;
    private long poolingPeakTime;

    public static final int DEFAULT_INITIAL_SIZE = 0;
    public static final int DEFAULT_MAX_ACTIVE_SIZE = 8;
    public static final int DEFAULT_MAX_IDLE = 8;
    public static final int DEFAULT_MIN_IDLE = 0;
    public static final int DEFAULT_MAX_WAIT = -1;

    protected volatile int initialSize = DEFAULT_INITIAL_SIZE;
    protected volatile int maxActive = DEFAULT_MAX_ACTIVE_SIZE;
    protected volatile int minIdle = DEFAULT_MIN_IDLE;
    protected volatile int maxIdle = DEFAULT_MAX_IDLE;
    protected volatile long maxWait = DEFAULT_MAX_WAIT;

    protected int notFullTimeoutRetryCount;

}
