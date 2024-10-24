package cn.piesat.framework.security.model;

import lombok.Data;

/**
 * <p/>
 * {@code @description}: License校验参数类
 * <p/>
 * {@code @create}: 2024-10-24 15:54
 * {@code @author}: zhouxp
 */

@Data
public class LicenseVerifyParam {
    /**
     * 证书subject
     */
    private String subject;

    /**
     * 公钥别称
     */
    private String publicAlias;

    /**
     * 访问公钥库的密码
     */
    private String storePass;

    /**
     * 证书生成路径
     */
    private String licensePath;

    /**
     * 密钥库存储路径
     */
    private String publicKeysStorePath;
}
