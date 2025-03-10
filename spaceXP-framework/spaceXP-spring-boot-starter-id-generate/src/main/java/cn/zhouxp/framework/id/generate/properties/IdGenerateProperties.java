package cn.zhouxp.framework.id.generate.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2025-03-09 13:57:35
 *
 * @author zhouxp
 */
@Data
@ConfigurationProperties(prefix = IdGenerateProperties.PREFIX)
public class IdGenerateProperties {
    public static final String PREFIX = "space.id.generate";
    ThreadPoolProperty thread;

    private Integer retryTimes = 3;
}
