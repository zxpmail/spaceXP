package cn.piesat.framework.common.model.enums;

import cn.piesat.framework.common.model.interfaces.IBaseResponse;
import cn.piesat.framework.common.properties.CommonEnumProperties;
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
    SUCCESS(CommonEnumProperties.successCode,CommonEnumProperties.successValue),
    /**
     * 操作失败时默认消息
     */
    ERROR(CommonEnumProperties.errorCode, CommonEnumProperties.errorValue),
    /**
     * 系统错误时，默认消息
     */
    SYS_ERROR(CommonEnumProperties.sysErrorCode, CommonEnumProperties.sysErrorValue),

    URL_LEN_INVALID(CommonEnumProperties.urlLenInvalidCode, CommonEnumProperties.urlLenInvalidValue),
    TOKEN_INVALID(CommonEnumProperties.tokenInvalidCode, CommonEnumProperties.tokenInvalidValue),

    TOKEN_EMPTY(CommonEnumProperties.tokenEmptyCode, CommonEnumProperties.tokenEmptyValue),

    TOKEN_KEY_EXPIRED(CommonEnumProperties.tokenKeyExpiredCode,CommonEnumProperties.tokenKeyExpiredValue),

    REFRESH_TOKEN_INVALID(CommonEnumProperties.refreshTokenInvalidCode, CommonEnumProperties.refreshTokenInvalidValue),

    AOP_ADVICE_ERROR(514,"AOP通知错误！"),
    ;
    /**
     * 响应状态码
     */
    private  Integer code;

    /**
     * 响应信息
     */
    private  String message;

    /**
     * 通过code获取message
     */
    public static String getMessage(Integer code) {
        CommonResponseEnum[] types = CommonResponseEnum.values();
        for (CommonResponseEnum type : types) {
            if (type.getCode().equals(code)) {
                return type.getMessage();
            }
        }
        return null;
    }
}
