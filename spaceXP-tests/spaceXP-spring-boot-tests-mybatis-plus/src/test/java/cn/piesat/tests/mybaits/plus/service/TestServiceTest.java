package cn.piesat.tests.mybaits.plus.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TestServiceTest {

    @Autowired
    private TestService testService;
    @Test
    public void testCustomAnnotation() {
        testService.insertUsersBySql();
        testService.insertUsersWithBatchProcessing();
        testService.insertUsersWithJdbcBatch();
        testService.insertUsersWithCustomBatch();
        testService.insertUsersOneByOne();
    }
}
