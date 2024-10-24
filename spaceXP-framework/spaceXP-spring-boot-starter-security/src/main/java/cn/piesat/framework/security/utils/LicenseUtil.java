package cn.piesat.framework.security.utils;


import cn.piesat.framework.security.model.CustomKeyStoreParam;
import cn.piesat.framework.security.model.CustomLicenseParam;
import de.schlichtherle.license.*;

import lombok.extern.slf4j.Slf4j;

import javax.security.auth.x500.X500Principal;
import java.io.File;

import java.util.prefs.Preferences;

/**
 * <p/>
 * {@code @description}: 证书工具类
 *  LicenseUtil.generateLicense(new CustomLicenseParam(),new X500Principal("CN=localhost,   OU=localhost,   O=localhost,   L=SH,   ST=SH,   C=CN"));
 * <p/>
 * {@code @create}: 2024-10-24 14:41
 * {@code @author}: zhouxp
 */
@Slf4j
public class LicenseUtil {

    //   生成License证书
    public static void generateLicense(CustomLicenseParam param,X500Principal holderIssuer) {
        try {
            LicenseManager licenseManager = new LicenseManager(initLicenseParam(param));
            LicenseContent licenseContent = initLicenseContent(param,holderIssuer);
            licenseManager.store(licenseContent, new File(param.getLicensePath()));
        } catch (Exception e) {
            log.error("证书生成失败", e);
        }
    }

    //   初始化证书生成参数
    private static LicenseParam initLicenseParam(CustomLicenseParam param ) {
        Preferences preferences = Preferences.userNodeForPackage(LicenseCreator.class);
        //   设置对证书内容加密的秘钥
        CipherParam cipherParam = new DefaultCipherParam(param.getStorePass());
        //   自定义KeyStoreParam
        KeyStoreParam privateStoreParam = new CustomKeyStoreParam(LicenseCreator.class
                , param.getPrivateKeysStorePath()
                , param.getPrivateAlias()
                , param.getStorePass()
                , param.getKeyPass());

        //   组织License参数
        return new DefaultLicenseParam(param.getSubject()
                , preferences
                , privateStoreParam
                , cipherParam);
    }

    //   设置证书生成正文信息
    private static LicenseContent initLicenseContent(CustomLicenseParam param,X500Principal holderIssuer) {
        LicenseContent licenseContent = new LicenseContent();
        licenseContent.setHolder(holderIssuer);
        licenseContent.setIssuer(holderIssuer);
        licenseContent.setSubject(param.getSubject());
        licenseContent.setIssued(param.getIssuedTime());
        licenseContent.setNotBefore(param.getIssuedTime());
        licenseContent.setNotAfter(param.getExpiryTime());
        licenseContent.setConsumerType(param.getConsumerType());
        licenseContent.setConsumerAmount(param.getConsumerAmount());
        licenseContent.setInfo(param.getDescription());
        return licenseContent;
    }
}
