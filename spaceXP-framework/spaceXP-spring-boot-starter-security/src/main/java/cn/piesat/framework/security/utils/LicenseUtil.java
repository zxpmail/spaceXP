package cn.piesat.framework.security.utils;


import cn.piesat.framework.security.core.LicenseManagerHolder;
import cn.piesat.framework.security.model.CustomKeyStoreParam;
import cn.piesat.framework.security.model.CustomLicenseParam;
import cn.piesat.framework.security.model.LicenseVerifyParam;
import de.schlichtherle.license.*;

import lombok.extern.slf4j.Slf4j;

import javax.security.auth.x500.X500Principal;
import java.io.File;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.prefs.Preferences;

/**
 * <p/>
 * {@code @description}: 证书工具类
  * <p/>
 * {@code @create}: 2024-10-24 14:41
 * {@code @author}: zhouxp
 */
@Slf4j
public class LicenseUtil {

    //   生成License证书
    public static void generateLicense(CustomLicenseParam param, X500Principal holderIssuer) {
        try {
            LicenseManager licenseManager = new LicenseManager(initLicenseParam(param));
            LicenseContent licenseContent = initLicenseContent(param, holderIssuer);
            licenseManager.store(licenseContent, new File(param.getLicensePath()));
        } catch (Exception e) {
            log.error("证书生成失败", e);
        }
    }

    //   初始化证书生成参数
    private static LicenseParam initLicenseParam(CustomLicenseParam param) {
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
    private static LicenseContent initLicenseContent(CustomLicenseParam param, X500Principal holderIssuer) {
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

    // 安装License证书
    public static synchronized LicenseContent install(LicenseVerifyParam param) {
        LicenseContent result = null;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 1. 安装证书
        try {
            LicenseManager licenseManager = LicenseManagerHolder.getInstance(initLicenseParam(param));
            licenseManager.uninstall();
            result = licenseManager.install(new File(param.getLicensePath()));
            log.info(MessageFormat.format("证书安装成功，证书有效期：{0} - {1}",
                    format.format(result.getNotBefore()), format.format(result.getNotAfter())));
        } catch (Exception e) {
            log.error("证书安装失败: {}", e.getMessage());
        }
        return result;
    }

    // 校验License证书
    public static boolean verify() {
        LicenseManager licenseManager = LicenseManagerHolder.getInstance(null);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 2. 校验证书
        try {
            LicenseContent licenseContent = licenseManager.verify();
            log.info(MessageFormat.format("证书校验通过，证书有效期：{0} - {1}",
                    format.format(licenseContent.getNotBefore()), format.format(licenseContent.getNotAfter())));
            return true;
        } catch (Exception e) {
            log.error("证书校验失败: {}", e.getMessage());
            return false;
        }
    }

    // 初始化证书生成参数
    private static LicenseParam initLicenseParam(LicenseVerifyParam param) {
        Preferences preferences = Preferences.userNodeForPackage(LicenseUtil.class);
        CipherParam cipherParam = new DefaultCipherParam(param.getStorePass());
        KeyStoreParam publicStoreParam = new CustomKeyStoreParam(LicenseUtil.class
                , param.getPublicKeysStorePath()
                , param.getPublicAlias()
                , param.getStorePass()
                , null);
        return new DefaultLicenseParam(param.getSubject()
                , preferences
                , publicStoreParam
                , cipherParam);
    }
}
