package cn.piesat.tools.gateway.properties;

import cn.piesat.framework.common.properties.TokenProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * <p/>
 * {@code @description}  :网关配置类
 * <p/>
 * <b>@create:</b> 2023/10/8 8:54.
 *
 * @author zhouxp
 */
@ConfigurationProperties(prefix = "login")
@Data
public class GatewayProperties {

    /**
     * 请求租户需要忽略拦截的url路径
     */
    private List<String> sassIgnorePaths;
    /**
     * 忽略拦截的url路径
     */
    private List<String> ignorePaths;

    /**
     * 没有登录是否进行网页重定向
     */
    private Boolean isRedirect =false;

    /**
     * 是否鉴权
     */
    private Boolean isAuthentication = true ;

    /**
     * token配置信息
     */
    private TokenProperties tokenProperties =new TokenProperties();

    /**
     * 是否开启日志打印
     */
    private Boolean enabled = true;

    private ApiAlarmConfiguration fail = new ApiAlarmConfiguration();

    private SlowApiAlarmConfiguration slow = new SlowApiAlarmConfiguration();

    /**
     * 慢API报警配置
     */
    @Data
    public static class SlowApiAlarmConfiguration {

        /**
         * 是否开启API慢日志打印
         */
        private boolean alarm = true;

        /**
         * 报警阈值 （单位：毫秒）
         */
        private long threshold = 500;
    }


    /**
     * API异常报警(根据http状态码判定）
     */
    @Data
    public static class ApiAlarmConfiguration {

        /**
         * 是否开启异常报警 默认关闭
         */
        private boolean alarm = false;

        /**
         * 排除状态码
         */
        private List<Integer> exclusion;
    }
}
