package cn.piesat.framework.redis.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p/>
 * {@code @description}  :Web配置信息
 * <p/>
 * <b>@create:</b> 2023/9/28 15:07.
 *
 * @author zhouxp
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "space.redis")
public class RedisProperties {

    /**
     * 消息机制是否开启
     */
    private Boolean messageEnable = false;

    /**
     * 消息主题
     */
    private String topics = "TOPIC";

    /**
     * 压缩是否开启
     */
    private Boolean compressEnable = false;

    /**
     * 防止重新刷新页面是否开启
     */
    private Boolean preventReplayEnable = false;

    /**
     * 限流拦截器是否开启
     */
    private Boolean accessLimitEnable = false;
    /**
     * 只拦截url 不对参数处理
     */
    private Boolean onlyUrl = false;

    /**
     * 访问健keyPrefix
     */
    private String keyPrefix = "access_";

    /**
     * hashKey是否为String
     */
    private Boolean hashKeyIsString = true;
}
