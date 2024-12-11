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
@ConfigurationProperties(prefix = "gateway")
@Data
public class GatewayProperties {

    /**
     * 登录信息
     */
    private Login login;
    /**
     * 是否开启日志打印
     */
    private Boolean logEnabled = true;

    @Data
    public static class Login {
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
        private Boolean isRedirect = false;

        /**
         * 是否鉴权
         */
        private Boolean isAuthentication = true;

        /**
         * token配置信息
         */
        private TokenProperties tokenProperties = new TokenProperties();
    }


    /**
     * 灰度信息
     */
    private Gray gray;

    /**
     * 是否启动灰度发布
     */
    private Boolean grayEnabled = true;

    @Data
    public static class Gray {
        /**
         * 生产的版本
         */
        private String version;
        /**
         * 需要灰度的人员列表
         */
        private List<String> users;

        /**
         * 灰度的版本
         */
        private String grayVersion;
    }
}
