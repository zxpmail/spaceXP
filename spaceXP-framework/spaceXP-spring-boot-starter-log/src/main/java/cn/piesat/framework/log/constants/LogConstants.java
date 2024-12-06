package cn.piesat.framework.log.constants;

/**
 * <p/>
 * {@code @description}  :日志常数类
 * <p/>
 * <b>@create:</b> 2023/10/8 14:31.
 *
 * @author zhouxp
 */
public interface LogConstants {
    /**
     * 异常
     */
    Integer error = 2;
    /**
     * 成功
     */
    Integer success = 1;
    /**
     * 操作
     */
    String OP ="op";

    /**
     * 描述
     */
    String DESCRIPTION ="description";
    /**
     * 拦截日志类型 1 opLog 2 swagger
     */
    Integer LOG_TYPE = 1;

    String INVALID_RET ="invalid return value";

    String INVALID_PARAMETER ="invalid parameter";

    String BROWSER ="client";

    String OS ="unknown";

    String SLOW_THRESHOLD_MILLS ="slowThresholdMills";
}
