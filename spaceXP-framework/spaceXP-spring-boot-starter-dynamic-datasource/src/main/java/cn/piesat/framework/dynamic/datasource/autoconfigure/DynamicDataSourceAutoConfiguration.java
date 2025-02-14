package cn.piesat.framework.dynamic.datasource.autoconfigure;

import cn.piesat.framework.dynamic.datasource.advisor.DynamicDataSourceAdvisor;
import cn.piesat.framework.dynamic.datasource.annotation.DS;
import cn.piesat.framework.dynamic.datasource.annotation.DSTransactional;
import cn.piesat.framework.dynamic.datasource.datasource.DynamicDataSource;
import cn.piesat.framework.dynamic.datasource.datasource.DynamicRoutingDataSource;
import cn.piesat.framework.dynamic.datasource.init.DataSourceInit;
import cn.piesat.framework.dynamic.datasource.init.DecryptDataSourceInit;
import cn.piesat.framework.dynamic.datasource.interceptor.DynamicDataSourceInterceptor;
import cn.piesat.framework.dynamic.datasource.interceptor.DynamicLocalTransactionInterceptor;
import cn.piesat.framework.dynamic.datasource.properties.DynamicDataSourceProperties;
import cn.piesat.framework.dynamic.datasource.provider.DynamicDataSourceProvider;
import cn.piesat.framework.dynamic.datasource.provider.YamlDynamicDataSourceProvider;
import cn.piesat.framework.dynamic.datasource.support.DataSourceClassResolver;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.Order;

import javax.sql.DataSource;
import java.util.List;

/**
 * <p/>
 * {@code @description}: 动态数据源自动装配类
 * <p/>
 * {@code @create}: 2025-02-10 9:35
 * {@code @author}: zhouxp
 */
@Configuration
@EnableConfigurationProperties(value = DynamicDataSourceProperties.class)
@Import(value = {DynamicDataSourceClassResolverAutoConfiguration.class, DynamicDataSourceCreatorAutoConfiguration.class})
@ConditionalOnProperty(prefix = DynamicDataSourceProperties.PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
public class DynamicDataSourceAutoConfiguration {
    private final DynamicDataSourceProperties dataSourceProperties;

    public DynamicDataSourceAutoConfiguration(DynamicDataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }

    @Order(0)
    @Bean
    public YamlDynamicDataSourceProvider yamlDynamicDataSourceProvider() {
        return new YamlDynamicDataSourceProvider(dataSourceProperties.getDataSource());
    }

    //当前不存在其它datasource才去装配
    @ConditionalOnMissingBean
    @Bean
    @Primary
    public DataSource dataSource(List<DynamicDataSourceProvider> providers) {
        DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource(providers);
        dataSource.setPrimary(this.dataSourceProperties.getPrimary());
        return dataSource;
    }

    @Bean
    public DataSourceInit dataSourceInit() {
        return new DecryptDataSourceInit();
    }

    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    public Advisor dynamicDataSourceAnnotationAdvisor(List<DataSourceClassResolver> resolvers) {
        DynamicDataSourceInterceptor advice = new DynamicDataSourceInterceptor(resolvers);
        return new DynamicDataSourceAdvisor(advice, DS.class);
    }

    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    public Advisor dynamicTransactionalDataSourceAnnotationAdvisor() {
//        DynamicTransactionalInterceptor advice = new DynamicTransactionalInterceptor();
//        return new DynamicDataSourceAdvisor(advice, DSTransactional.class);
        DynamicLocalTransactionInterceptor interceptor = new DynamicLocalTransactionInterceptor(false);
        return new DynamicDataSourceAdvisor(interceptor, DSTransactional.class);
    }
    @Bean
    public DynamicDataSource dynamicDataSource() {
        return new DynamicDataSource();
    }
}
