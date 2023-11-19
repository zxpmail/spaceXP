package cn.piesat.framework.web.enums;

import cn.piesat.framework.common.model.interfaces.IBaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p/>
 * {@code @description}  : web响应枚举通用类
 * <p/>
 * <b>@create:</b> 2023/9/27 8:48.
 *
 * @author zhouxp
 */
@Getter
@AllArgsConstructor
public enum WebResponseEnum implements IBaseResponse {
    SERVLET_ERROR(503,"服务器ServerLet内部错误"),
    INVALID_INPUT(504,"参数输入有误"),

    INVALID_DATETIME(505,"无效时间"),

    ;

    /**
     * 响应状态码
     */
    private final Integer code;

    /**
     * 响应信息
     */
    private final String message;
}
