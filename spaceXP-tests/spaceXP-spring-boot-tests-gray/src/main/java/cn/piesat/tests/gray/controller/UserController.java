package cn.piesat.tests.gray.controller;


import cn.piesat.tests.gray.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author zhouxp
 * @email zhouxiaoping@piesat.cn
 * @date 2022-10-08 14:43:40
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/gray/user")
@RequiredArgsConstructor
public class UserController {

    @Value("${server.port}")
    private Integer port;
    private final UserService userService;

    @ApiOperation("登录")
    @PostMapping("login")
    public Map<String, Object> login(@RequestParam String userName, @RequestParam String password) {
        return userService.login(userName, password);
    }

    @GetMapping(value = "testGray")
    public String testGray() {
        return "port=" + port;
    }
}
