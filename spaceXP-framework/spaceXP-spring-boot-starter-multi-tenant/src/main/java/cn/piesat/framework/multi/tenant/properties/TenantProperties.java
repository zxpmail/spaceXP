package cn.piesat.framework.multi.tenant.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * {@code @description}  :多租配置信息
 * <p/>
 * <b>@create:</b> 2023/9/5 14:02.
 *
 * @author zhouxp
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "space.tenant")
public class TenantProperties {

    /**
     * 数据库中多租标志字段
     */
    private String tenantIdColumn ="tenant_id";

    /**
     * 配置不进行多租户隔离的表名
     */
    private List<String> ignoreTables = new ArrayList<>();

    /**
     * 配置不进行多租户隔离的sql
     * 需要配置mapper的全路径如：cn.hello.hello.dao.mapper.UserMapper.selectList
     */
    private List<String> ignoreSql = new ArrayList<>();

    /**
     * 配置不进行多租户隔离的用户
     */
    private List<String> ignoreUser = new ArrayList<>();
}

