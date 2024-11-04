package cn.piesat.framework.log.external;

/**
 * <p/>
 * {@code @description}: 动态日志配置接口
 * <p/>
 * {@code @create}: 2024-11-04 16:17
 * {@code @author}: zhouxp
 */
public interface DynamicLogging {
    /**
     * 设置日志级别
     * @param packageName 包名
     * @param level 日志级别
     */
    void setLogLevel(String packageName, String level);

    /**
     * 重置日志级别
     * @param packageName 包名
     */
    void resetLogLevel(String packageName);
}
