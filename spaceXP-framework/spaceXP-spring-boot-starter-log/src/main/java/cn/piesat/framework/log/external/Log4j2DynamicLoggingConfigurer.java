package cn.piesat.framework.log.external;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import static org.apache.logging.log4j.Level.*;

/**
 * <p/>
 * {@code @description}: log4j2动态日志配置
 * <p/>
 * {@code @create}: 2024-11-05 9:05
 * {@code @author}: zhouxp
 */
public class Log4j2DynamicLoggingConfigurer implements DynamicLoggingConfigurer{
    @Override
    public void setLogLevel(String packageName, String level) {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        Configuration config = context.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig(packageName);
        if (loggerConfig != null) {
            switch (level.toUpperCase()) {
                case "TRACE":
                    loggerConfig.setLevel(TRACE);
                    break;
                case "DEBUG":
                    loggerConfig.setLevel(DEBUG);
                    break;
                case "INFO":
                    loggerConfig.setLevel(INFO);
                    break;
                case "WARN":
                    loggerConfig.setLevel(WARN);
                    break;
                case "ERROR":
                    loggerConfig.setLevel(ERROR);
                    break;
                default:
                    loggerConfig.setLevel(ALL);
                    break;
            }
            context.updateLoggers();
        }
    }

    @Override
    public void resetLogLevel(String packageName) {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        Configuration config = context.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig(packageName);
        if (loggerConfig != null) {
            loggerConfig.setLevel(null); // 重置为默认级别
            context.updateLoggers();
        }
    }
}
