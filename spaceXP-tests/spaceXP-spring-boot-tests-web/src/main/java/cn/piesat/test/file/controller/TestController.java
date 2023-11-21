package cn.piesat.test.file.controller;


import cn.piesat.framework.common.annotation.LoginUser;
import cn.piesat.framework.common.annotation.NoApiResult;
import cn.piesat.framework.common.model.dto.JwtUser;
import cn.piesat.framework.security.annotation.DecryptMethod;
import cn.piesat.framework.security.annotation.EncryptMethod;
import cn.piesat.test.file.model.entity.DesensitizeDO;
import cn.piesat.test.file.model.entity.EncryptDO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p/>
 * {@code @description}  :测试
 * <p/>
 * <b>@create:</b> 2023/1/10 10:06.
 *
 * @author zhouxp
 */
@RestController
@Api(tags = "测试信息")
@RequestMapping("/test")
public class TestController {

    /**
     * 测试根据配置文件修改枚举
     */
    @ApiOperation("测试根据配置文件修改枚举")
    @GetMapping("/changeEnum")
    public String changeEnum() {
        return "ok";
    }

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
    @GetMapping("get")
    @NoApiResult
    public String test(){
        return "hello";
    }

    @GetMapping("get1")
    public String test1(){
        throw new RuntimeException("hh");
    }

    @GetMapping("get2")
    public JwtUser test2(@LoginUser JwtUser jwtUser){
        return jwtUser;
    }
}
