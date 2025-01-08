package cn.piesat.framework.common.constants;

/**
 * <p/>
 * {@code @description}  : 通用常量
 * <p/>
 * <b>@create:</b> 2023/9/25 10:44.
 *
 * @author zhouxp
 */
public interface CommonConstants {

    /**
     * 默认除法运算精度
     */
    int DEF_DIV_SCALE = 10;
    /**
     * 请求路径
     */
    String URI = "uri";

    String USER_ID = "userId";

    Long ILLEGAL_USER_ID = -9999L;
    /**
     * 传递部门ID
     */
    String DEPT_ID = "deptId";

    /**
     * 多租户ID
     */
    String TENANT_ID ="tenantId";
    /**
     * 部门名称
     */
    String DEPT_NAME = "deptName";
    /**
     * 消息提示
     */
    String MESSAGE = "模块:{},消息:{}";

    /**
     * header token标识
     */
    String TOKEN = "token";

    /**
     * token user
     */
    String USERNAME = "userName";

    /**
     * token user
     */
    String USER = "user";
    /**
     * token object
     */
    String SUBJECT = "SPACE-XP-USER";

    String SASS="space-sass";

    String  FEIGN_NATIVE="Response";

    String UNDERLINE="_";

    String COLON=":";

    String OS ="os";

    String BROWSER="browser";

    String IP="ip";
    /**
     * 应用Id
     */
    String APP_ID="appId";

    String PARAMETERS_NULL ="参数不能为空！";

    String Unsupported_type="不支持类型";

    String SIGNATURE_ARGUMENT ="参数不能为空!";

    String COMMA =",";

    /**
     * 请求url
     */
    String REQ_URL = "reqUrl";

    /**
     * 请求参数
     */
    String REQ_PARAMETER = "reqParameter";

    /**
     * 流程ID
     */
    String FLOW_ID = "flowId";

    /**
     * 操作流程类型
     */

    String FLOW_TYPE = "flowType";

    /**
     * 操作流程名称
     */

    String FLOW_NAME = "flowName";

    /**
     * 应用名称
     */
    String APP_NAME = "appName";

    /**
     * 公钥
     */
    String PUBLIC_KEY="publicKey";

    /**
     * 私钥
     */
    String PRIVATE_KEY="privateKey";
}
