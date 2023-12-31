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
     * 是否进行JSON LocalDateTime时间处理格式化
     */
    private Boolean dateFormatterEnable = false;

    /**
     * dateTime格式
     */
    private String dateTimePattern = "yyyy-MM-dd HH:mm:ss";

    /**
     * date格式
     */
    private String datePattern = "yyyy-MM-dd";
    /**
     * 忽略进行统一返回url
     */
    private List<String> ignoreUrls;

}
