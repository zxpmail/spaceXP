package cn.piesat.framework.dynamic.datasource.properties;

import cn.piesat.framework.dynamic.datasource.model.DataSourceEntity;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;


/**
 * <p/>
 * {@code @description}  :数据权限属性类
 * <p/>
 * <b>@create:</b> 2023/9/6 13:18.
 *
 * @author zhouxp
 */
@ConfigurationProperties(prefix = "space.datasource")
@Data
public class DataSourceProperties {
    private Map<String,DataSourceEntity> dss;

    private String pointcut;
}
