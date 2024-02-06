package cn.piesat.framework.knife4j.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p/>
 * {@code @description}  :配置
 * <p/>
 * <b>@create:</b> 2023/10/7 9:20.
 *
 * @author zhouxp
 */
@ConfigurationProperties("space.swagger")
@Data
public class SwaggerProperties {
    private String title="API测试文档";

    private String description="接口测试文档";

    private String author="zhouxiaoping";

    private String version="1.1";

    private String url="https://piesat.cn";

    private String email="zhouxiaoping@piesat.cn";

    private String pathsToMatch ="/**/**";
}
