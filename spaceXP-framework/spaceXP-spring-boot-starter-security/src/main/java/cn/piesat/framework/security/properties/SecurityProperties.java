package cn.piesat.framework.security.properties;

import cn.piesat.framework.security.model.LicenseVerifyParam;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p/>
 * {@code @description}  :安全配置项信息
 * <p/>
 * <b>@create:</b> 2023/10/9 13:17.
 *
 * @author zhouxp
 */
@Data
@ConfigurationProperties(prefix = "space.security")
public class SecurityProperties {
    private String secretKey = "0123456789abcdef";

    private String iv = "abcdefghijklmnop";

    private Boolean enable = false;

    private License license;

    private Boolean licenseEnable = false;

    @Data
    public static class License {
        /**
         * 证书subject
         */
        private String subject = "license";
        /**
         * 公钥别称
         */
        private String publicAlias = "publicCert";
        /**
         * 访问公钥库的密码
         */
        private String storePass = "public_password1234";

        /**
         * 证书文件
         */
        private String licenseFile = "d:/license/license.lic";

        /**
         * 公钥文件
         */
        private String publicKeysFile = "d:/license/publicCerts.keystore";
    }

}
