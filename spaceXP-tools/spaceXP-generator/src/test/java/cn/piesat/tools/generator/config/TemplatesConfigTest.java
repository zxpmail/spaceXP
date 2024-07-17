package cn.piesat.tools.generator.config;



import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * <p/>
 * {@code @description}: 模板配置测试类
 * <p/>
 * {@code @create}: 2024-07-16 14:55
 * {@code @author}: zhouxp
 */
@SpringBootTest
public class TemplatesConfigTest {
    @Resource
    private TemplatesConfig templatesConfig;

    @Test
    public void TestTemplatesConfig(){
        assertEquals(templatesConfig.getTemplates().size(),17);
        assertNotNull(templatesConfig.getDeveloper());
        assertNotNull(templatesConfig.getProject());
    }
}
