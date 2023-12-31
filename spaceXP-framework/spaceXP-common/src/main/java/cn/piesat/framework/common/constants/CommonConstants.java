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
     * 请求路径
     */
    String URI = "uri";

    String USER_ID = "userId";

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
}
