package cn.piesat.framework.test.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * {@code @description}  :日志配置类
 * <p/>
 * <b>@create:</b> 2023/10/8 13:34.
 *
 * @author zhouxp
 */
@Data
@ConfigurationProperties("space.test")
public class TestProperties {
    /**
     * 枚举字段名称，以逗号分隔
     */
    private String enumFieldsNameSuffix="status";
    /**
     * 枚举字段值
     */
    private List<Integer> enumFieldsValue=new ArrayList<Integer>(){{add(0);add(1);}};

    /**
     * 小整数字段名称，以逗号分隔
     */
    private String tinyIntFieldsNameSuffix="status";


    /**
     * 默认值为false的bool字段名称，以逗号分隔
     */
    private String falseFieldsNameSuffix="deleted";
}
