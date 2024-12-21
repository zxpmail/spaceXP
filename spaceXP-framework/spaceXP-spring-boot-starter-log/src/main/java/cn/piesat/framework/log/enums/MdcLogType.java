package cn.piesat.framework.log.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <p/>
 * {@code @description}: 增加mdc日志类型
 * <p/>
 * {@code @create}: 2024-12-21 15:29
 * {@code @author}: zhouxp
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum MdcLogType {
    /**
     * 其他日志
     */
    OTHER("0","OTHER"),

    /**
     * 审计日志
     */
    AUDIT("1","AUDIT"),

    /**
     * 业务日志
     */
    BIZ("2","BUSINESS"),

    /**
     * 异常日志
     */
    FAIL("3","EXCEPTION"),

    /**
     * 应用日志
     */
    APP("4","APP"),

    ;
    /**
     * 操作代码
     */
    private String code;
    /**
     * 操作
     */
    private String op;
}
