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
@ConfigurationProperties(prefix = "space.log.external")
public class LogExternalProperties {

    /**
     * 不需要网关的rest 调用地址
     */
    private String restUrlPrefix="http://localhost:8006/log";

    /**
     * 日志服务名
     */
    private String logServerName = "log";

    /**
     * 请求日志http保存地址
     */
    private String save ="/log/add";

    /**
     * 是否启用restTemplate模式
     */
    private Boolean restTemplateEnabled = true;
}
