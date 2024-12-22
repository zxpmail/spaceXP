package cn.piesat.framework.log.filter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import org.springframework.util.StringUtils;

import static cn.piesat.framework.log.constants.MdcLogConstants.LOG_TYPE;


/**
 * <p/>
 * {@code @description}: 日志类型过滤器：含有logType标志记录日志否则不记录日志
 * <p/>
 * {@code @create}: 2024-12-22 9:33
 * {@code @author}: zhouxp
 */
public class LogTypeFilter extends Filter<ILoggingEvent> {
    @Override
    public FilterReply decide(ILoggingEvent event) {
        String s = event.getMDCPropertyMap().get(LOG_TYPE);
        if (StringUtils.hasText(s)){
            return FilterReply.ACCEPT;
        }
        return FilterReply.DENY;
    }
}
