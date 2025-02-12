package cn.piesat.framework.dynamic.datasource.autoconfigure;

import cn.piesat.framework.dynamic.datasource.creator.DataSourceCreator;
import cn.piesat.framework.dynamic.datasource.creator.DruidDataSourceCreator;
import cn.piesat.framework.dynamic.datasource.creator.HikariDataSourceCreator;
import cn.piesat.framework.dynamic.datasource.creator.LoadDataSourceCreator;
import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

import static cn.piesat.framework.dynamic.datasource.constants.DataSourceConstant.DRUID_ORDER;
import static cn.piesat.framework.dynamic.datasource.constants.DataSourceConstant.HIKARI_ORDER;

/**
 * <p/>
 * {@code @description}: 动态数据源自动装配类
 * <p/>
 * {@code @create}: 2025-02-10 9:09
 * {@code @author}: zhouxp
 */
@Configuration
public class DynamicDataSourceCreatorAutoConfiguration {
    @Bean
    public LoadDataSourceCreator defaultDataSourceCreator(List<DataSourceCreator> creators) {
        return new LoadDataSourceCreator(creators);
    }

    @ConditionalOnClass(DruidDataSource.class)
    @Configuration
    static class DruidDataSourceCreatorConfiguration {
        @Order(DRUID_ORDER)
        @Bean("DruidDataSourceCreator")
        public DruidDataSourceCreator dataSourceCreator() {
            return new DruidDataSourceCreator();
        }
    }

    @ConditionalOnClass(HikariDataSource.class)
    @Configuration
    static class HikariDataSourceCreatorConfiguration {
        @Order(HIKARI_ORDER)
        @Bean("HikariDataSourceCreator")
        public HikariDataSourceCreator dataSourceCreator() {
            return new HikariDataSourceCreator();
        }
    }
}
