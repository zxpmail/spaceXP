package cn.piesat.framework.web.properties;

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
@ConfigurationProperties(prefix = "space.web")
public class WebProperties {
    /**
     * 是否进行返回值处理包装处理
     */

    private Boolean returnValueEnable = true;

    /**
     * 是否进行统一异常处理
     */
    private Boolean webExceptionEnable = true;
    /**
     * 是否统计打印初始化bean时间
     */

    private Boolean costEnable = false;
    /**
     * 是否绑定登录用户信息
     */

    private Boolean LoginUserEnable = true;


    /**
     * dateTime格式
     */
    private String dateTimePattern = "yyyy-MM-dd HH:mm:ss";

    /**
     * date格式
     */
    private String datePattern = "yyyy-MM-dd";

    /**
     * time 格式
     */
    private String timePattern = "HH:mm:ss";
    /**
     * 忽略进行统一返回url
     */
    private List<String> ignoreUrls;

    /**
     * 过滤开关
     */
    private Boolean xssEnable = true;
    /**
     * 排除链接（多个用逗号分隔）
     */
    private String excludes;

    /**
     * 匹配链接(多个用逗号分隔）
     */
    private String urlPatterns = "/*";
    /**
     * Long bigInteger json转成String
     */
    private boolean jacksonCustomize = true;

    /**
     * service 参数校验是否生效
     */
    private boolean serviceValidationEnable = true;

    /**
     * 参数错误是否显示详细信息
     */

    private Boolean parameterErrorEnable = true;
}
