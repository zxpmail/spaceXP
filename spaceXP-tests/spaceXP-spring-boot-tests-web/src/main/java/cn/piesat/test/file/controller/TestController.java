package cn.piesat.test.file.controller;


import cn.piesat.framework.common.annotation.LoginUser;
import cn.piesat.framework.common.annotation.NoApiResult;
import cn.piesat.framework.common.model.dto.JwtUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

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
@Slf4j
public class TestController {
    /**
     * 测试根据配置文件修改枚举
     */
    @ApiOperation("测试根据配置文件修改枚举")
    @GetMapping("/changeEnum")
    public String changeEnum() {
        return "ok";
    }


    @GetMapping("get")
    @NoApiResult
    public String test() {
        return "hello";
    }

    @GetMapping("get1")
    public String test1() {
        throw new RuntimeException("hh");
    }

    @GetMapping("get2")
    public JwtUser test2(@LoginUser JwtUser jwtUser) {
        return jwtUser;
    }

    @GetMapping("assert")
    public void testAssert() {
        Assert.notNull(null, "Value must not be null");
    }

    private static int count = 0;

    @GetMapping("hello")
    public String hello() throws InterruptedException {
        count++;
        if (count % 10 == 9) {
            Thread.sleep(100000000);
        }
        return "Hello, World!";
    }

    @ApiOperation(value = "导入")
    @PostMapping("/import")
    public void importFile(@RequestPart MultipartFile file) {
    }

    @GetMapping("map")
    public Map<String, Long> map() {
        return new HashMap<String, Long>() {{
            put("k1", 1L);
            put("k2", 2L);
        }};
    }

    //实体
    @PostMapping("modelXssFilter")
    public UserEntity modelXssFilter(@RequestBody UserEntity user) {
        log.error(user.getUsername() + "---" + user.getMobile());
        return user;
    }

    /**
     *
     <a href="http://localhost:8080/test/testXss?param=1">...</a>
     http://localhost:8080/test/testXss?param="参数"
     http://localhost:8080/test/testXss?param=<aside><a href="#" target="_blank">链接</a></aside>
     http://localhost:8080/test/testXss?param=<script>alert("1")</script>
     */
    //不转义
    @GetMapping("/testXss")
    public void openXssFilter(String param) {
        log.error(param);
    }

    @Data
    static
    class UserEntity {
        private Integer userId;

        private String username;

        private String password;

        private String mobile;
    }
}
