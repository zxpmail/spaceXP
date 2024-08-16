package cn.piesat.tests.user.controller;

import cn.piesat.framework.common.constants.CommonConstants;
import cn.piesat.tests.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 * 
 *
 * @author zhouxp
 * @email zhouxiaoping@piesat.cn
 * @date 2022-10-08 14:43:40
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @ApiOperation("登录")
    @PostMapping("login")
    public Map<String, Object> login(@RequestParam String userName, @RequestParam String password) {
        return userService.login(userName,password);
    }

    @ApiOperation("退出登录")
    @GetMapping("logout")
    public Boolean logout(@RequestHeader(CommonConstants.USER_ID) Long userId) {
        return userService.logout(userId);
    }
}
