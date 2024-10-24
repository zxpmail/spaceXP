package cn.piesat.framework.security.core;

import cn.piesat.framework.common.context.AbstractValidateItem;
import cn.piesat.framework.security.model.LicenseVerifyParam;
import cn.piesat.framework.security.properties.SecurityProperties;
import cn.piesat.framework.security.utils.LicenseUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * <p/>
 * {@code @description}:
 * <p/>
 * {@code @create}: 2024-10-24 16:29
 * {@code @author}: zhouxp
 */
@Slf4j
public class InstallLicense extends AbstractValidateItem {
    private final SecurityProperties.License license;

    public InstallLicense(SecurityProperties.License license) {
        this.license = license;
    }

    @Override
    public void validate() {
        log.info("++++++++ 开始安装证书 ++++++++");
        LicenseVerifyParam param = new LicenseVerifyParam();
        param.setSubject(license.getSubject());
        param.setPublicAlias(license.getPublicAlias());
        param.setStorePass(license.getStorePass());
        // 相对路径resources资源目录
        String resourcePath = Objects.requireNonNull(getClass().getClassLoader().getResource("")).getPath();
        // 证书地址
        param.setLicensePath(resourcePath + "license.lic");
        // 公钥地址
        param.setPublicKeysStorePath(resourcePath + "publicCerts.keystore");
        // 安装证书

        LicenseUtil.install(param);
        log.info("++++++++ 证书安装结束 ++++++++");
    }
}
