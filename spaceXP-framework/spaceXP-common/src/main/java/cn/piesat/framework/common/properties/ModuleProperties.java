package cn.piesat.framework.common.properties;

/**
 * <p/>
 * {@code @description}  :模块属性配置信息
 * <p/>
 * <b>@create:</b> 2023/1/13 9:03.
 *
 * @author zhouxp
 */

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "space.module")
@Data
public class ModuleProperties {
    private String module = "test";
}
