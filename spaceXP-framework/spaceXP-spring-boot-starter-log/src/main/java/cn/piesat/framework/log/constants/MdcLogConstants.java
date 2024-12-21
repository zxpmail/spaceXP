package cn.piesat.framework.log.constants;

/**
 * <p/>
 * {@code @description}: Mdc常量类
 * <p/>
 * {@code @create}: 2024-12-21 14:47
 * {@code @author}: zhouxp
 */
public interface MdcLogConstants {

    String CALL_STACK = "callStack";
    String TRACE_ID = "traceId";

    String RESULT = "result";

    String LOG_TYPE = "logType";


    String LOG_TYPE_CODE = "logTypeCode";

    String DEFAULT_PATTERN = "%d{yyyy-MM-dd HH:mm:ss.SSSXXX} [%thread] %-5level %logger{36} - [%t] %p %logger - %msg%n%xEx";
}
