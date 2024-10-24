package cn.piesat.framework.security.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * <p/>
 * {@code @description}: License参数类
 * <p/>
 * {@code @create}: 2024-10-24 14:26
 * {@code @author}: zhouxp
 */
@Data
public class CustomLicenseParam {
    /**
     * 证书subject
     */
    private String subject = "license";

    /**
     * 密钥别称
     */
    private String privateAlias = "privateKey";

    /**
     * 公钥密码（需要妥善保管，不能让使用者知道）
     */
    private String keyPass = "private_password1234";

    /**
     * 私钥库的密码
     */
    private String storePass = "public_password1234";

    /**
     * 证书生成路径
     */
    private String licensePath = "d:/license/license.lic";

    /**
     * 私钥存储路径
     */
    private String privateKeysStorePath = "d:/license/privateKeys.keystore";

    /**
     * 证书生效时间
     */
    @JsonFormat(pattern = "yyyy-MM-ddHH:mm:ss", timezone = "GMT+8")
    private Date issuedTime = new Date();

    /**
     * 证书失效时间
     */
    @JsonFormat(pattern = "yyyy-MM-ddHH:mm:ss", timezone = "GMT+8")
    private Date expiryTime = Date.from(LocalDateTime.now().plusYears(1L).atZone(ZoneId.systemDefault()).toInstant());

    /**
     * 用户类型
     */
    private String consumerType = "user";

    /**
     * 用户数量
     */
    private Integer consumerAmount = 1;

    /**
     * 描述信息
     */
    private String description = "证书描述信息";
}
