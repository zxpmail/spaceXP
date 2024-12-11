package cn.piesat.tools.gateway.constant;

/**
 * @author zhouxp
 */
public interface GatewayConstant {
    String HEADER_NAME="Content-Type";
    String HEADER_VALUE="application/json;charset=UTF-8";
    /**
     * 模块名称
     */
    String MODULE ="gateway";
    String IP = "ip";
    String UNKNOWN = "unknown";

    String X_REAL_IP = "X-Real-IP";

    String HTTP_CLIENT_IP = "HTTP_CLIENT_IP";

    String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";

    String PROXY_CLIENT_IP = "Proxy-Client-IP";

    String X_FORWARDED_FOR = "X-Forwarded-For";

    String defaultName ="test";

    String UTF8="UTF-8";

    String WS_TOKEN = "Sec-Websocket-Protocol";
    String APP_ID = "1";

    /**
     * 常规输出
     */
    String APPLICATION_JSON_REQUEST = "applicationJsonRequest";

    String FORM_DATA_REQUEST = "formDataRequest";

    String BASIC_REQUEST = "basicRequest";

    String NORMAL_REQUEST = "normalRequest";

    /**
     * 慢查询
     */
    String SLOW = "slow";

    /**
     * 非200响应
     */
    String FAIL = "fail";

    /**
     * 灰度发布标志
     */
    String GRAY_LB_FLAG = "grayLb";

    /**
     * 灰度发布版本
     */
    String VERSION ="version";

}
