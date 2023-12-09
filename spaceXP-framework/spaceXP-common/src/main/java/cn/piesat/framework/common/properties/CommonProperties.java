package cn.piesat.framework.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p/>
 * {@code @description}  :通用属性类
 * <p/>
 * <b>@create:</b> 2023/11/28 8:58.
 *
 * @author zhouxp
 */
@ConfigurationProperties(prefix = "space.common")
public final class CommonProperties {

    public static Result result;

    public  Result getResult() {
        return result;
    }

    public  void setResult(Result result) {
        CommonProperties.result = result;
    }

    public final static class Result {
        public static String code = "code";
        public static String message = "message";
        public static String data = "data";

        public String getCode () {
            return code;
        }

        public void setCode (String code){
            Result.code = code;
        }

        public String getMessage () {
            return message;
        }

        public void setMessage (String message){
            Result.message = message;
        }

        public String getData () {
            return data;
        }

        public void setData (String data){
            Result.data = data;
        }
    }

    public static ResponseCode responseCode;

    public  ResponseCode getResponseCode() {
        return responseCode;
    }

    public  void setResponseCode(ResponseCode responseCode) {
        CommonProperties.responseCode = responseCode;
    }

    public final static class ResponseCode {
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

        public static Integer aopAdviceErrorCode =514;
        public static String  aopAdviceErrorValue ="AOP通知错误！";

        public static Integer noPermissionDataCode =515;
        public static String noPermissionDataValue ="没有数据权限";

        public static Integer badSqlGrammarErrorCode =503;
        public static String badSqlGrammarErrorValue ="不支持当前数据库";

        public static Integer recordRepeatCode =504;
        public static String recordRepeatValue ="数据库中已存在该记录";

        public static Integer queryDataErrorCode =505;
        public static String queryDataErrorValue ="数据处理错误";

        public static Integer datasourceErrorCode =516;

        public  static String datasourceErrorValue ="数据源错误";

        public  Integer getDatasourceErrorCode() {
            return datasourceErrorCode;
        }

        public  void setDatasourceErrorCode(Integer datasourceErrorCode) {
            ResponseCode.datasourceErrorCode = datasourceErrorCode;
        }

        public  String getDatasourceErrorValue() {
            return datasourceErrorValue;
        }

        public  void setDatasourceErrorValue(String datasourceErrorValue) {
            ResponseCode.datasourceErrorValue = datasourceErrorValue;
        }


        public  Integer getSuccessCode() {
            return successCode;
        }

        public  void setSuccessCode(Integer successCode) {
            ResponseCode.successCode = successCode;
        }

        public  String getSuccessValue() {
            return successValue;
        }

        public  void setSuccessValue(String successValue) {
            ResponseCode.successValue = successValue;
        }

        public  Integer getErrorCode() {
            return errorCode;
        }

        public  void setErrorCode(Integer errorCode) {
            ResponseCode.errorCode = errorCode;
        }

        public  String getErrorValue() {
            return errorValue;
        }

        public  void setErrorValue(String errorValue) {
            ResponseCode.errorValue = errorValue;
        }

        public  Integer getSysErrorCode() {
            return sysErrorCode;
        }

        public  void setSysErrorCode(Integer sysErrorCode) {
            ResponseCode.sysErrorCode = sysErrorCode;
        }

        public  String getSysErrorValue() {
            return sysErrorValue;
        }

        public  void setSysErrorValue(String sysErrorValue) {
            ResponseCode.sysErrorValue = sysErrorValue;
        }

        public  Integer getUrlLenInvalidCode() {
            return urlLenInvalidCode;
        }

        public  void setUrlLenInvalidCode(Integer urlLenInvalidCode) {
            ResponseCode.urlLenInvalidCode = urlLenInvalidCode;
        }

        public  String getUrlLenInvalidValue() {
            return urlLenInvalidValue;
        }

        public  void setUrlLenInvalidValue(String urlLenInvalidValue) {
            ResponseCode.urlLenInvalidValue = urlLenInvalidValue;
        }

        public  Integer getTokenInvalidCode() {
            return tokenInvalidCode;
        }

        public  void setTokenInvalidCode(Integer tokenInvalidCode) {
            ResponseCode.tokenInvalidCode = tokenInvalidCode;
        }

        public  String getTokenInvalidValue() {
            return tokenInvalidValue;
        }

        public  void setTokenInvalidValue(String tokenInvalidValue) {
            ResponseCode.tokenInvalidValue = tokenInvalidValue;
        }

        public  Integer getTokenEmptyCode() {
            return tokenEmptyCode;
        }

        public  void setTokenEmptyCode(Integer tokenEmptyCode) {
            ResponseCode.tokenEmptyCode = tokenEmptyCode;
        }

        public  String getTokenEmptyValue() {
            return tokenEmptyValue;
        }

        public  void setTokenEmptyValue(String tokenEmptyValue) {
            ResponseCode.tokenEmptyValue = tokenEmptyValue;
        }

        public  Integer getTokenKeyExpiredCode() {
            return tokenKeyExpiredCode;
        }

        public  void setTokenKeyExpiredCode(Integer tokenKeyExpiredCode) {
            ResponseCode.tokenKeyExpiredCode = tokenKeyExpiredCode;
        }

        public  String getTokenKeyExpiredValue() {
            return tokenKeyExpiredValue;
        }

        public  void setTokenKeyExpiredValue(String tokenKeyExpiredValue) {
            ResponseCode.tokenKeyExpiredValue = tokenKeyExpiredValue;
        }

        public  Integer getRefreshTokenInvalidCode() {
            return refreshTokenInvalidCode;
        }

        public  void setRefreshTokenInvalidCode(Integer refreshTokenInvalidCode) {
            ResponseCode.refreshTokenInvalidCode = refreshTokenInvalidCode;
        }

        public  String getRefreshTokenInvalidValue() {
            return refreshTokenInvalidValue;
        }

        public  void setRefreshTokenInvalidValue(String refreshTokenInvalidValue) {
            ResponseCode.refreshTokenInvalidValue = refreshTokenInvalidValue;
        }

        public  Integer getAopAdviceErrorCode() {
            return aopAdviceErrorCode;
        }

        public  void setAopAdviceErrorCode(Integer aopAdviceErrorCode) {
            ResponseCode.aopAdviceErrorCode = aopAdviceErrorCode;
        }

        public  String getAopAdviceErrorValue() {
            return aopAdviceErrorValue;
        }

        public  void setAopAdviceErrorValue(String aopAdviceErrorValue) {
            ResponseCode.aopAdviceErrorValue = aopAdviceErrorValue;
        }

        public  Integer getNoPermissionDataCode() {
            return noPermissionDataCode;
        }

        public  void setNoPermissionDataCode(Integer noPermissionDataCode) {
            ResponseCode.noPermissionDataCode = noPermissionDataCode;
        }

        public  String getNoPermissionDataValue() {
            return noPermissionDataValue;
        }

        public  void setNoPermissionDataValue(String noPermissionDataValue) {
            ResponseCode.noPermissionDataValue = noPermissionDataValue;
        }

        public  Integer getBadSqlGrammarErrorCode() {
            return badSqlGrammarErrorCode;
        }

        public  void setBadSqlGrammarErrorCode(Integer badSqlGrammarErrorCode) {
            ResponseCode.badSqlGrammarErrorCode = badSqlGrammarErrorCode;
        }

        public  String getBadSqlGrammarErrorValue() {
            return badSqlGrammarErrorValue;
        }

        public  void setBadSqlGrammarErrorValue(String badSqlGrammarErrorValue) {
            ResponseCode.badSqlGrammarErrorValue = badSqlGrammarErrorValue;
        }

        public  Integer getRecordRepeatCode() {
            return recordRepeatCode;
        }

        public  void setRecordRepeatCode(Integer recordRepeatCode) {
            ResponseCode.recordRepeatCode = recordRepeatCode;
        }

        public  String getRecordRepeatValue() {
            return recordRepeatValue;
        }

        public  void setRecordRepeatValue(String recordRepeatValue) {
            ResponseCode.recordRepeatValue = recordRepeatValue;
        }

        public  Integer getQueryDataErrorCode() {
            return queryDataErrorCode;
        }

        public  void setQueryDataErrorCode(Integer queryDataErrorCode) {
            ResponseCode.queryDataErrorCode = queryDataErrorCode;
        }

        public  String getQueryDataErrorValue() {
            return queryDataErrorValue;
        }

        public  void setQueryDataErrorValue(String queryDataErrorValue) {
            ResponseCode.queryDataErrorValue = queryDataErrorValue;
        }
    }
}
