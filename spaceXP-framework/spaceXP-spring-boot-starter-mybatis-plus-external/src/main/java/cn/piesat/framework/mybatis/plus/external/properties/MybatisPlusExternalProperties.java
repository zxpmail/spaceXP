package cn.piesat.framework.mybatis.plus.external.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p/>
 * {@code @description}  :mybatisplus额外配置项
 * <p/>
 * <b>@create:</b> 2024-06-06 13:46.
 *
 * @author zhouxp
 */
@ConfigurationProperties(prefix = "space.db.external")
@Data
public class MybatisPlusExternalProperties {
    /**
     * 工作区id 最大为99
     */
    private Integer workId = 1;

    /**
     * key值前置
     */
    private String keyPrefix="piesat";

    /**
     * key长度
     */
    private Integer length = 6;
}
