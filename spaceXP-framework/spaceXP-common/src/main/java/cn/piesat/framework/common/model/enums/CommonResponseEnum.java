package cn.piesat.framework.common.model.enums;

import cn.piesat.framework.common.model.interfaces.IBaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <p/>
 * {@code @description}  : 通用响应码类
 * <p/>
 * <b>@create:</b> 2023/9/25 9:09.
 *
 * @author zhouxp
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum CommonResponseEnum implements IBaseResponse {
    /**
     * 操作成功时默认消息
     */
    SUCCESS(200,"操作成功"),
    /**
     * 操作失败时默认消息
     */
    ERROR(500, "操作失败"),
    /**
     * 系统错误时，默认消息
     */
    SYS_ERROR(501, "服务器内部错误,请与管理员联系！"),

    URL_LEN_INVALID(502, "uri长度无效"),
    TOKEN_INVALID(510, "token无效"),

    TOKEN_EMPTY(511, "token为空"),

    TOKEN_KEY_EXPIRED(512,"密钥已过时"),

    REFRESH_TOKEN_INVALID(513, "刷新Token无效"),

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
