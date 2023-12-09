package cn.piesat.framework.common.model.enums;

import cn.piesat.framework.common.model.interfaces.IBaseResponse;
import cn.piesat.framework.common.properties.CommonProperties;
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
    SUCCESS(CommonProperties.ResponseCode.successCode,CommonProperties.ResponseCode.successValue),
    /**
     * 操作失败时默认消息
     */
    ERROR(CommonProperties.ResponseCode.errorCode, CommonProperties.ResponseCode.errorValue),
    /**
     * 系统错误时，默认消息
     */
    SYS_ERROR(CommonProperties.ResponseCode.sysErrorCode, CommonProperties.ResponseCode.sysErrorValue),

    URL_LEN_INVALID(CommonProperties.ResponseCode.urlLenInvalidCode, CommonProperties.ResponseCode.urlLenInvalidValue),
    TOKEN_INVALID(CommonProperties.ResponseCode.tokenInvalidCode, CommonProperties.ResponseCode.tokenInvalidValue),

    TOKEN_EMPTY(CommonProperties.ResponseCode.tokenEmptyCode, CommonProperties.ResponseCode.tokenEmptyValue),

    TOKEN_KEY_EXPIRED(CommonProperties.ResponseCode.tokenKeyExpiredCode,CommonProperties.ResponseCode.tokenKeyExpiredValue),

    REFRESH_TOKEN_INVALID(CommonProperties.ResponseCode.refreshTokenInvalidCode, CommonProperties.ResponseCode.refreshTokenInvalidValue),

    AOP_ADVICE_ERROR(CommonProperties.ResponseCode.aopAdviceErrorCode,CommonProperties.ResponseCode.aopAdviceErrorValue),

    NO_PERMISSION_DATA(CommonProperties.ResponseCode.noPermissionDataCode,CommonProperties.ResponseCode.noPermissionDataValue),

    BAD_SQL_GRAMMAR_ERROR(CommonProperties.ResponseCode.badSqlGrammarErrorCode,CommonProperties.ResponseCode.badSqlGrammarErrorValue),
    RECORD_REPEAT(CommonProperties.ResponseCode.recordRepeatCode,CommonProperties.ResponseCode.recordRepeatValue),

    QUERY_DATA(CommonProperties.ResponseCode.queryDataErrorCode,CommonProperties.ResponseCode.queryDataErrorValue),

    DATASOURCE_ERROR(CommonProperties.ResponseCode.datasourceErrorCode,CommonProperties.ResponseCode.datasourceErrorValue),
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
