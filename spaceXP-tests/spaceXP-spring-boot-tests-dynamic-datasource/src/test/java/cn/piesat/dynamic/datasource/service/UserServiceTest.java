package cn.piesat.dynamic.datasource.service;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2023/12/28 14:46.
 *
 * @author zhouxp
 */
@SpringBootTest
public class UserServiceTest {
    @Resource
    private UserService userService;
    @Test
    void contextLoads() {
        System.out.println(userService.info(1L));
    }
}
