package cn.piesat.framework.log.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * <p/>
 * {@code @description}  :日志配置类
 * <p/>
 * <b>@create:</b> 2023/10/8 13:34.
 *
 * @author zhouxp
 */
@Data
@ConfigurationProperties("space.log")
public class LogProperties {
    /**
     * 使用日志标志类 1使用OpLog注解 2使用swagger注解
     */
    private Integer logFlag = 1;

    /**
     * 从header中获取值写入日志
     */
    private List<String> headers;

}
