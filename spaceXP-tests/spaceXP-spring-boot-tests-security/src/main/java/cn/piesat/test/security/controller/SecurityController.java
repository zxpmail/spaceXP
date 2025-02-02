package cn.piesat.test.security.controller;

import cn.piesat.framework.security.annotation.DecryptMethod;
import cn.piesat.framework.security.annotation.EncryptMethod;
import cn.piesat.framework.security.model.CustomLicenseParam;
import cn.piesat.framework.security.utils.LicenseUtil;
import cn.piesat.test.security.model.entity.DesensitizeDO;
import cn.piesat.test.security.model.entity.EncryptDO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.x500.X500Principal;

/**
 * <p/>
 * {@code @description}  :安全组件测试
 * <p/>
 * <b>@create:</b> 2023/11/22 10:55.
 *
 * @author zhouxp
 */
@Api(tags = "安全组件测试")
@RestController
@RequestMapping("security")
@RequiredArgsConstructor
public class SecurityController {
    @ApiOperation("测试脱敏")
    @GetMapping("desensitize")
    public DesensitizeDO desensitize() {
        DesensitizeDO userInfo = new DesensitizeDO();
        userInfo.setName("张三");
        userInfo.setEmail("1859656863@qq.com");
        userInfo.setPhone("15286535426");
        userInfo.setCustom("12345678");
        return userInfo;
    }

    @ApiOperation("测试加密")
    @PostMapping("encrypt")
    @EncryptMethod
    public EncryptDO encrypt(@RequestBody EncryptDO encryptDO) {
        return encryptDO;
    }

    @ApiOperation("测试解密")
    @PostMapping("decrypt")
    @DecryptMethod
    public EncryptDO decrypt() {
        EncryptDO encryptDO = new EncryptDO();
        encryptDO.setAge(12);
        encryptDO.setName("E7798C31C4A94F2C43DE78FE8D050D40");
        return encryptDO;
    }

    @ApiOperation("生成认证")
    @GetMapping("generateLicense")
    public String generateLicense() {
        LicenseUtil.generateLicense(new CustomLicenseParam(), new X500Principal("CN=localhost,   OU=localhost,   O=localhost,   L=SH,   ST=SH,   C=CN"));
        return "证书生成成功！";
    }
}
