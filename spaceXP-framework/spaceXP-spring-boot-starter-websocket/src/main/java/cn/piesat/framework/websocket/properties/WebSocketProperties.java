package cn.piesat.framework.websocket.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * <p/>
 * {@code @description}: 配置类
 * <p/>
 * {@code @create}: 2024-06-19 10:03
 * {@code @author}: zhouxp
 */
@Data
@ConfigurationProperties(prefix = "space.websocket")
public class WebSocketProperties {
    /**
     * 调试模式 单用户 不进行登录验证
     */
    private Boolean debug = true;

    /**
     * 处理路径
     */
    private String handlerPath;

    /**
     * 是否允许跨域
     */
     private Boolean allowedOrigin ;

    /**
     * 允许跨域
     */
    private String domainNames ="*" ;


}
