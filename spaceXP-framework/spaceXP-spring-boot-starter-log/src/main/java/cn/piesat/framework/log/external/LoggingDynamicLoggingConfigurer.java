package cn.piesat.framework.log.external;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p/>
 * {@code @description}: logging动态配置类
 * <p/>
 * {@code @create}: 2024-11-05 9:51
 * {@code @author}: zhouxp
 */
public class LoggingDynamicLoggingConfigurer implements DynamicLoggingConfigurer{
    @Override
    public void setLogLevel(String packageName, String level) {
        Logger logger = Logger.getLogger(packageName);
        if (logger != null) {
            switch (level.toUpperCase()) {
                case "TRACE":
                    logger.setLevel(Level.FINER);
                    break;
                case "DEBUG":
                    logger.setLevel(Level.FINE);
                    break;
                case "INFO":
                    logger.setLevel(Level.INFO);
                    break;
                case "WARN":
                    logger.setLevel(Level.WARNING);
                    break;
                case "ERROR":
                    logger.setLevel(Level.SEVERE);
                    break;
                default:
                    logger.setLevel(Level.ALL);
                    break;
            }
        }
    }

    @Override
    public void resetLogLevel(String packageName) {
        Logger logger = Logger.getLogger(packageName);
        if (logger != null) {
            logger.setLevel(null); // 重置为默认级别
        }
    }
}
