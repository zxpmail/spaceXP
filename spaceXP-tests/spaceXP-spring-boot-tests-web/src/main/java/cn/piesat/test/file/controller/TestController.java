package cn.piesat.test.file.controller;


import cn.piesat.framework.common.annotation.LoginUser;
import cn.piesat.framework.common.annotation.NoApiResult;
import cn.piesat.framework.common.exception.BaseException;
import cn.piesat.framework.common.model.dto.JwtUser;
import cn.piesat.framework.common.model.enums.CommonResponseEnum;
import cn.piesat.framework.common.properties.CommonEnumProperties;
import org.springframework.web.bind.annotation.GetMapping;
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
public class TestController {

    @GetMapping("/changeEnum")
    public String changeEnum() {
        return "ok";
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
