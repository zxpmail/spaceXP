package cn.piesat.framework.multi.tenant.config;


import cn.piesat.framework.common.model.dto.TwoDTO;
import cn.piesat.framework.multi.tenant.core.MultiTenantInterceptor;
import cn.piesat.framework.multi.tenant.core.TenantFilter;
import cn.piesat.framework.multi.tenant.properties.TenantProperties;
import cn.piesat.framework.multi.tenant.utils.TenantContextHolder;
import cn.piesat.framework.mybatis.plus.config.MybatisPlusConfig;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * {@code @description}  :数据权限配置
 * <p/>
 * <b>@create:</b> 2023/1/11 11:27.
 *
 * @author zhouxp
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({TenantProperties.class})
@AutoConfigureBefore(MybatisPlusConfig.class)
@Primary
public class MultiTenantConfig {
    @Bean
    public TenantLineHandler tenantLineHandler(TenantProperties tenantProperties) {
        return new TenantLineHandler() {
            @Override
            public String getTenantIdColumn() {
                return tenantProperties.getTenantIdColumn();
            }

            /**
             * 获取租户id
             */
            @Override
            public Expression getTenantId() {
                TwoDTO<Long, String> tenant = TenantContextHolder.getTenant();
                if (!ObjectUtils.isEmpty(tenant) && !ObjectUtils.isEmpty(tenant.getFirst())) {
                    return new LongValue(TenantContextHolder.getTenant().getFirst());
                }
                return new NullValue();
            }

            /**
             * 过滤不需要根据租户隔离的表
             * @param tableName 表名
             */
            @Override
            public boolean ignoreTable(String tableName) {
                TwoDTO<Long, String> tenant = TenantContextHolder.getTenant();
                if(ObjectUtils.isEmpty(TenantContextHolder.getTenant()) || ObjectUtils.isEmpty(TenantContextHolder.getTenant().getFirst())){
                    return true;
                }
                List<String> ignoreUser = tenantProperties.getIgnoreUser();
                if (CollectionUtils.isNotEmpty(ignoreUser) && !ObjectUtils.isEmpty(tenant) && !ObjectUtils.isEmpty(tenant.getSecond())) {
                    if (ignoreUser.contains(tenant.getSecond())) {
                        return true;
                    }
                }
                return tenantProperties.getIgnoreTables().stream().anyMatch(
                        (e) -> e.equalsIgnoreCase(tableName)
                );
            }

        };
    }

    @Bean
    public TenantFilter tenantFilter(MybatisPlusInterceptor plusInterceptor, TenantLineHandler tenantLineHandler, TenantProperties tenantProperties){
        List<InnerInterceptor> interceptors =new ArrayList<>();
        MultiTenantInterceptor tenantInterceptor = new MultiTenantInterceptor(tenantLineHandler, tenantProperties);
        interceptors.add(tenantInterceptor);
        interceptors.addAll(plusInterceptor.getInterceptors());
        plusInterceptor.setInterceptors(interceptors);
        return new TenantFilter();
    }

}
