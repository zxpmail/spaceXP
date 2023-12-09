package cn.piesat.framework.dynamic.datasource.config;

import cn.piesat.framework.dynamic.datasource.core.DynamicDataSource;
import cn.piesat.framework.dynamic.datasource.model.DataSourceEntity;
import cn.piesat.framework.dynamic.datasource.properties.DataSourceProperties;
import cn.piesat.framework.dynamic.datasource.utils.DataSourceUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * {@code @description}  : 数据源配置类
 * <p/>
 * <b>@create:</b> 2023/12/9 14:48.
 *
 * @author zhouxp
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({DataSourceProperties.class})
public class DataSourceConfiguration {
    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSource masterDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "dynamicDataSource")
    public DynamicDataSource createDynamicDataSource(DataSource masterDataSource,DataSourceProperties dataSourceProperties){
        Map<Object,Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("__master",masterDataSource);
        for (DataSourceEntity dss : dataSourceProperties.getDss()) {
            DataSource test = DataSourceUtils.test(dss);
            dataSourceMap.put(dss.getKey(),test);
        }
        return new DynamicDataSource(masterDataSource,dataSourceMap);
    }
}
