package cn.piesat.framework.permission.data.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.Set;

/**
 * <p/>
 * {@code @description}  :数据权限属性类
 * <p/>
 * <b>@create:</b> 2023/9/6 13:18.
 *
 * @author zhouxp
 */
@ConfigurationProperties(prefix = "space.permission.data")
@Data
public class DataPermissionProperties {

    /**
     * 优先查询条件，如果有条件则只对条件进行数据权限拦截，否则对全部数据进行拦截，配置那些方法执行权限控制 如mapper或者mapper中的方法
     */
    private Set<String> conditions = Collections.emptySet();
    /**
     * 配置那些方法不执行权限控制 如mapper或者mapper中的方法
     */
    private Set<String> ignoreConditions = Collections.emptySet();
    /**
     * 指定那些sql不执行权限控制
     */
    private Set<String> ignoreUsers = Collections.emptySet();

    /**
     * 指定创建人id的字段名
     */
    private String creatorIdColumnName = "creator";

    /**
     * 指定部门id的字段名
     */
    private String deptIdColumnName = "dept_id";
}
