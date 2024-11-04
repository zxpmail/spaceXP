package cn.piesat.framework.log.external;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;

/**
 * <p/>
 * {@code @description}: Logback动态日志配置
 * <p/>
 * {@code @create}: 2024-11-04 16:25
 * {@code @author}: zhouxp
 */
public class LogbackDynamicLoggingConfigurer implements DynamicLoggingConfigurer {
    @Override
    public void setLogLevel(String packageName, String level) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = context.getLogger(packageName);
        if (logger != null) {
            switch (level.toUpperCase()) {
                case "TRACE":
                    logger.setLevel(Level.TRACE);
                    break;
                case "DEBUG":
                    logger.setLevel(Level.DEBUG);
                    break;
                case "INFO":
                    logger.setLevel(Level.INFO);
                    break;
                case "WARN":
                    logger.setLevel(Level.WARN);
                    break;
                case "ERROR":
                    logger.setLevel(Level.ERROR);
                    break;
                default:
                    logger.setLevel(Level.ALL);
                    break;
            }
        }
    }
    @Override
    public void resetLogLevel(String packageName) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = context.getLogger(packageName);
        if (logger != null) {
            logger.setLevel(null); // 重置为默认级别
        }
    }
}
