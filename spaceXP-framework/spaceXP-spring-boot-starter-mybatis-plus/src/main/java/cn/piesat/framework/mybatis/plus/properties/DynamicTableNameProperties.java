package cn.piesat.framework.mybatis.plus.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * <p/>
 * {@code @description}  :配置属性
 * <p/>
 * <b>@create:</b> 2023/1/12 17:14.
 *
 * @author zhouxp
 */
@ConfigurationProperties(prefix = "space.table")
@Data
public class DynamicTableNameProperties {
    /**
     * 表名前缀
     */
    private List<String> tablePrefix;

    /**
     * 是否开启动态表名
     */
    private Boolean enable=false;
}
