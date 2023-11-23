package cn.piesat.framework.permission.url.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * <p/>
 * {@code @description}  :URL权限属性
 * <p/>
 * <b>@create:</b> 2023/9/12 13:42.
 *
 * @author zhouxp
 */
@Data
@ConfigurationProperties(prefix = "space.permission.url")
public class UrlPermissionProperties {
    /**
     * 不进行权限拦截url地址
     */
    private List<String> whiteList;

    /**
     * 不进行权限拦截用户
     */
    private List<String> userList;

    /**
     * 过滤路径
     */
    private String urlPatterns ="/*";
    /**
     * 权限名称
     */
    private String name="urlPermissionFilter";

}
