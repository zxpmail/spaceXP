package cn.piesat.tests.user.properties;

import cn.piesat.framework.common.properties.TokenProperties;
import lombok.Data;
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
public class AdminProperties {

    /**
     * token配置信息
     */
    private TokenProperties tokenProperties =new TokenProperties();

    private Long menuId =1003L;
}
