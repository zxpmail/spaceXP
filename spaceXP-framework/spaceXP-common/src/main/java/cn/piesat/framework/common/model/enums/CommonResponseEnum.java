package cn.piesat.framework.common.model.enums;

import cn.piesat.framework.common.model.interfaces.IBaseResponse;
import cn.piesat.framework.common.properties.CommonProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

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

    RPC_ERROR(CommonProperties.ResponseCode.rpcError,CommonProperties.ResponseCode.rpcErrorValue),

    /**
     * 404 Web 服务器找不到您所请求的文件或脚本。请检查URL 以确保路径正确。
     */
    NOT_FOUND(CommonProperties.ResponseCode.notFound,
            String.format("哎呀，无法找到这个资源啦(%s)", HttpStatus.NOT_FOUND.getReasonPhrase())),

    /**
     * 405 对于请求所标识的资源，不允许使用请求行中所指定的方法。请确保为所请求的资源设置了正确的 MIME 类型。
     */
    METHOD_NOT_ALLOWED(CommonProperties.ResponseCode.methodNotAllowed,
            String.format("请换个姿势操作试试(%s)", HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase())),

    /**
     * 415 Unsupported Media Type
     */
    UNSUPPORTED_MEDIA_TYPE(CommonProperties.ResponseCode.unsupportedMediaType,
            String.format("呀，不支持该媒体类型(%s)", HttpStatus.UNSUPPORTED_MEDIA_TYPE.getReasonPhrase())),

    PARAM_ERROR(CommonProperties.ResponseCode.paramErrorCode, CommonProperties.ResponseCode.paramErrorValue),

    BUSINESS_ERROR(CommonProperties.ResponseCode.businessErrorCode,CommonProperties.ResponseCode.businessErrorValue),
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
