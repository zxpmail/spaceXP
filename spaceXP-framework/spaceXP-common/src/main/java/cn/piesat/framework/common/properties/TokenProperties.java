package cn.piesat.framework.common.properties;

import lombok.Data;

/**
 * <p/>
 * {@code @description}  :Token配置信息
 * <p/>
 * <b>@create:</b> 2023/10/8 8:37.
 *
 * @author zhouxp
 */
@Data
public class TokenProperties {
    /**
     * token过期时间
     */
    private Long expiration = 24 * 60 * 60 * 1000L;
    /**
     * token认证key
     */
    private String tokenSignKey="Z1h2o3uixp123456Z1h2o3uixp123456Z1h2o3uixp123456";
    /**
     * token存redis前缀
     */
    private String loginToken="login:token:";
}
