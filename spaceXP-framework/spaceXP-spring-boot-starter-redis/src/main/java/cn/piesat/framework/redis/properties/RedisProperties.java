package cn.piesat.framework.redis.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

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

    private Boolean messageEnable = false; //消息机制是否开启

    private String topics = "TOPIC";

    private Boolean compressEnable = false; //压缩是否开启

    private Boolean preventReplayEnable = false; //防止重新刷新页面是否开启

    private Boolean accessLimitEnable = false; //限流拦截器是否开启
}
