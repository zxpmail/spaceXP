package cn.piesat.tests.test.doman;


import cn.piesat.framework.test.core.AutoSetPojo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;


/**
 * <p/>
 * {@code @description}: 模板配置测试类
 * <p/>
 * {@code @create}: 2024-07-16 14:55
 * {@code @author}: zhouxp
 */
@SpringBootTest
public class UserDTOTest {


    @Test
    public void testCustomAnnotation() {
        UserDTO userDTO = AutoSetPojo.randomPojo(UserDTO.class);
        Assert.notNull(userDTO,"The pojo cannot be null!");
    }
}
