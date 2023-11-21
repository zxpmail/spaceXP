package cn.piesat.framework.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p/>
 * {@code @description}  :安全配置项信息
 * <p/>
 * <b>@create:</b> 2023/10/9 13:17.
 *
 * @author zhouxp
 */
@Data
@ConfigurationProperties(prefix = "space.security")
public class SecurityProperties {
    private String secretKey = "123456";
    private Boolean enable = false;
}
