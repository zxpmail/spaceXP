package cn.piesat.framework.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p/>
 * {@code @description}  : 通用枚举配置类
 * <p/>
 * <b>@create:</b> 2023/11/21 10:11.
 *
 * @author zhouxp
 */
@ConfigurationProperties(prefix = "space.enum")
public class CommonEnumProperties {
    public static Integer successCode= 200;
    public static String successValue ="操作成功";

    public static Integer errorCode =500;
    public static String errorValue ="操作失败";

    public static Integer sysErrorCode =501;
    public static String sysErrorValue ="服务器内部错误,请与管理员联系！";

    public static Integer urlLenInvalidCode =502;
    public static String urlLenInvalidValue ="uri长度无效";

    public static Integer tokenInvalidCode =510;
    public static String tokenInvalidValue ="token无效";

    public static Integer tokenEmptyCode =511;
    public static String tokenEmptyValue ="token为空";

    public static Integer tokenKeyExpiredCode =512;
    public static String tokenKeyExpiredValue ="密钥已过时";

    public static Integer refreshTokenInvalidCode =513;
    public static String refreshTokenInvalidValue ="刷新Token无效";


    public  Integer getSuccessCode() {
        return successCode;
    }

    public  void setSuccessCode(Integer successCode) {
        CommonEnumProperties.successCode = successCode;
    }

    public  String getSuccessValue() {
        return successValue;
    }

    public  void setSuccessValue(String successValue) {
        CommonEnumProperties.successValue = successValue;
    }

    public  Integer getErrorCode() {
        return errorCode;
    }

    public  void setErrorCode(Integer errorCode) {
        CommonEnumProperties.errorCode = errorCode;
    }

    public  String getErrorValue() {
        return errorValue;
    }

    public  void setErrorValue(String errorValue) {
        CommonEnumProperties.errorValue = errorValue;
    }

    public  Integer getSysErrorCode() {
        return sysErrorCode;
    }

    public  void setSysErrorCode(Integer sysErrorCode) {
        CommonEnumProperties.sysErrorCode = sysErrorCode;
    }

    public  String getSysErrorValue() {
        return sysErrorValue;
    }

    public  void setSysErrorValue(String sysErrorValue) {
        CommonEnumProperties.sysErrorValue = sysErrorValue;
    }

    public  Integer getUrlLenInvalidCode() {
        return urlLenInvalidCode;
    }

    public  void setUrlLenInvalidCode(Integer urlLenInvalidCode) {
        CommonEnumProperties.urlLenInvalidCode = urlLenInvalidCode;
    }

    public  String getUrlLenInvalidValue() {
        return urlLenInvalidValue;
    }

    public  void setUrlLenInvalidValue(String urlLenInvalidValue) {
        CommonEnumProperties.urlLenInvalidValue = urlLenInvalidValue;
    }

    public  Integer getTokenInvalidCode() {
        return tokenInvalidCode;
    }

    public  void setTokenInvalidCode(Integer tokenInvalidCode) {
        CommonEnumProperties.tokenInvalidCode = tokenInvalidCode;
    }

    public  String getTokenInvalidValue() {
        return tokenInvalidValue;
    }

    public  void setTokenInvalidValue(String tokenInvalidValue) {
        CommonEnumProperties.tokenInvalidValue = tokenInvalidValue;
    }

    public  Integer getTokenEmptyCode() {
        return tokenEmptyCode;
    }

    public  void setTokenEmptyCode(Integer tokenEmptyCode) {
        CommonEnumProperties.tokenEmptyCode = tokenEmptyCode;
    }

    public  String getTokenEmptyValue() {
        return tokenEmptyValue;
    }

    public  void setTokenEmptyValue(String tokenEmptyValue) {
        CommonEnumProperties.tokenEmptyValue = tokenEmptyValue;
    }

    public  Integer getTokenKeyExpiredCode() {
        return tokenKeyExpiredCode;
    }

    public  void setTokenKeyExpiredCode(Integer tokenKeyExpiredCode) {
        CommonEnumProperties.tokenKeyExpiredCode = tokenKeyExpiredCode;
    }

    public  String getTokenKeyExpiredValue() {
        return tokenKeyExpiredValue;
    }

    public  void setTokenKeyExpiredValue(String tokenKeyExpiredValue) {
        CommonEnumProperties.tokenKeyExpiredValue = tokenKeyExpiredValue;
    }

    public  Integer getRefreshTokenInvalidCode() {
        return refreshTokenInvalidCode;
    }

    public  void setRefreshTokenInvalidCode(Integer refreshTokenInvalidCode) {
        CommonEnumProperties.refreshTokenInvalidCode = refreshTokenInvalidCode;
    }

    public  String getRefreshTokenInvalidValue() {
        return refreshTokenInvalidValue;
    }

    public  void setRefreshTokenInvalidValue(String refreshTokenInvalidValue) {
        CommonEnumProperties.refreshTokenInvalidValue = refreshTokenInvalidValue;
    }
}
