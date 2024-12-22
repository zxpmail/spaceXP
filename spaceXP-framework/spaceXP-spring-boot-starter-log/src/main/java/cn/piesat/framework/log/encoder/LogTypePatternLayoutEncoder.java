package cn.piesat.framework.log.encoder;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.PatternLayoutEncoderBase;
import cn.piesat.framework.log.constants.MdcLogConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

import java.util.Map;

import static cn.piesat.framework.log.constants.MdcLogConstants.LOG_TYPE;

/**
 * <p/>
 * {@code @description}:日志模版编码
 * <p/>
 * {@code @create}: 2024-12-22 9:57
 * {@code @author}: zhouxp
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LogTypePatternLayoutEncoder extends PatternLayoutEncoderBase<ILoggingEvent> {
    private final PatternLayout patternLayout = new PatternLayout();

    String business;

    String app;

    String audit;

    String exception;

    String other;

    String unknown;

    @Override
    public void start() {
        patternLayout.setContext(context);

        patternLayout.setOutputPatternAsHeader(outputPatternAsHeader);


        super.start();
    }

    private void layout(String logType) {
        if (!StringUtils.hasText(logType)) {
            setPatternOrDefault(null);
            return;
        }

        String pattern = getPatternForLogType(logType);
        setPatternOrDefault(pattern);
    }

    private void setPatternOrDefault(String pattern) {
        if (StringUtils.hasText(pattern)) {
            patternLayout.setPattern(pattern);
        } else {
            if (StringUtils.hasText(unknown)) {
                patternLayout.setPattern(unknown);
            } else {
                patternLayout.setPattern(MdcLogConstants.DEFAULT_PATTERN);
            }
        }
    }

    private String getPatternForLogType(String logType) {
        switch (logType) {
            case "AUDIT":
                return audit;
            case "BUSINESS":
                return business;
            case "EXCEPTION":
                return exception;
            case "APP":
                return app;
            case "OTHER":
                return other;
            default:
                return null;
        }
    }

    @Override
    public byte[] encode(ILoggingEvent event) {
        String logType = "unknown";
        Map<String, String> currentMdcContext = null;
        try {
            Map<String, String> mdcPropertyMap = event.getMDCPropertyMap();
            if (mdcPropertyMap != null && !mdcPropertyMap.isEmpty()) {
                logType = mdcPropertyMap.get(LOG_TYPE);
                currentMdcContext = MDC.getCopyOfContextMap();
            }
            layout(logType);
            patternLayout.start();
            this.layout = patternLayout;
            return super.encode(event);
        } finally {
            if (currentMdcContext != null) {
                MDC.setContextMap(currentMdcContext);
            } else {
                MDC.clear();
            }
        }

    }
}

