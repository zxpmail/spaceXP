package cn.piesat.framework.log.external.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * <p/>
 * {@code @description}  :网关配置类
 * <p/>
 * <b>@create:</b> 2023/10/8 8:54.
 *
 * @author zhouxp
 */
@Component
@Data
@ConfigurationProperties(prefix = "space.log")
public class ToolsLogProperties {

    private String restUrlPrefix="http://192.168.2.100:38001/domain/";

}
