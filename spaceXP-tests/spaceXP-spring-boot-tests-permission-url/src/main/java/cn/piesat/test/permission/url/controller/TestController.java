package cn.piesat.test.permission.url.controller;


import org.springframework.web.bind.annotation.GetMapping;

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
@RequestMapping("/test")
public class TestController {
    @GetMapping("get1")
    public String get1(){
        return "hello1";
    }
    @GetMapping("get2")
    public String get2(){
        return "hello2";
    }
    @GetMapping("get3")
    public String get3(){
        return "hello3";
    }
}
