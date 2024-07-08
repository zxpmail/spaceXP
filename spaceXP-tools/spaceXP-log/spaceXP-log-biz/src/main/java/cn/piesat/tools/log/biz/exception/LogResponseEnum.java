package cn.piesat.tools.log.biz.exception;


import cn.piesat.framework.common.model.interfaces.IBaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2023/9/4 15:17.
 *
 * @author zhouxp
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum LogResponseEnum implements IBaseResponse {

    /**
     * =============================日志信息================================
     */
    LOG_START_LT_END_TIME(10801,"开始时间必须小于等于结束时间！"),

    ;
    /**
     * 响应状态码
     */
    private  Integer code;

    /**
     * 响应信息
     */
    private  String message;
}
