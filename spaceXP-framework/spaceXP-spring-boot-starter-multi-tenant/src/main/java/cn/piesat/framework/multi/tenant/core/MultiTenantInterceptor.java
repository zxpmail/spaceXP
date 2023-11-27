package cn.piesat.framework.multi.tenant.core;

import cn.piesat.framework.multi.tenant.properties.TenantProperties;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;


/**
 * <p/>
 * {@code @description}  :多租处理器
 * <p/>
 * <b>@create:</b> 2023/9/5 14:12.
 *
 * @author zhouxp
 */
public class MultiTenantInterceptor extends TenantLineInnerInterceptor {
    private final TenantProperties tenantProperties;

    public MultiTenantInterceptor(TenantLineHandler tenantLineHandler, TenantProperties tenantProperties) {
        super(tenantLineHandler);
        this.tenantProperties = tenantProperties;
    }

    /**
     * 1、判断是否忽略配置用户
     * 2、判断是否忽略语句或不存在tenant_id
     */
    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {

        if (isIgnoreMappedStatement(ms.getId())) {
            return;
        }
        PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
        mpBs.sql(parserSingle(mpBs.sql(), null));
    }

    private boolean isIgnoreMappedStatement(String msId) {
        return tenantProperties.getIgnoreSql().stream().anyMatch((e) -> e.equalsIgnoreCase(msId));
    }


}
