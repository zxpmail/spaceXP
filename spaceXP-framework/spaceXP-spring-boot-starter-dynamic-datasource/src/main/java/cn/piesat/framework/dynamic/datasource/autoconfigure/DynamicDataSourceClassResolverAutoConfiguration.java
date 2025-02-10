package cn.piesat.framework.dynamic.datasource.autoconfigure;

import cn.piesat.framework.dynamic.datasource.support.ClassAndMethodDataSourceResolver;
import cn.piesat.framework.dynamic.datasource.support.DataSourceClassResolver;
import cn.piesat.framework.dynamic.datasource.support.InterfaceAndMethodDataSourceResolver;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * {@code @description}: 解析数据源自动配置类
 * <p/>
 * {@code @create}: 2025-02-10 9:33
 * {@code @author}: zhouxp
 */
public class DynamicDataSourceClassResolverAutoConfiguration {
    /**
     * 解析数据源列表
     */
    @Bean
    public List<DataSourceClassResolver> dataSourceClassResolvers() {
        List<DataSourceClassResolver> resolvers = new ArrayList<>();
        resolvers.add(new ClassAndMethodDataSourceResolver());
        resolvers.add(new InterfaceAndMethodDataSourceResolver());
        return resolvers;
    }
}
