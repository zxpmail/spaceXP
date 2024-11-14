package cn.piesat.test.common.controller;



import cn.piesat.test.common.model.User;
import cn.piesat.test.common.service.UserService;
import com.github.javafaker.Faker;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;


import javax.annotation.Resource;
import java.util.Locale;


/**
 * <p/>
 * {@code @description}  :测试
 * <p/>
 * <b>@create:</b> 2023/1/10 10:06.
 *
 * @author zhouxp
 */
@RestController
@RequestMapping("pipeline")
public class PipelineController {
    @Resource
    private UserService userService;


    @GetMapping
    public User test() {
        Faker faker = Faker.instance(Locale.CHINA);
        User user = User.builder().age(20)
                .fullname(faker.name().fullName())
                .mobile(faker.phoneNumber().phoneNumber())
                .password("123456").build();
        userService.save(user);
        return user;
    }
}