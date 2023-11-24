package cn.piesat.framework.permission.data.config;

import cn.piesat.framework.mybatis.plus.config.MybatisPlusConfig;
import cn.piesat.framework.permission.data.core.DataPermissionFilter;
import cn.piesat.framework.permission.data.core.DataPermissionHandler;
import cn.piesat.framework.permission.data.properties.DataPermissionProperties;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

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
@EnableConfigurationProperties({DataPermissionProperties.class})
@AutoConfigureBefore(MybatisPlusConfig.class)
@Primary
public class DataPermissionAutoConfig {
    @Bean
    public DataPermissionFilter dataScopeFilter(MybatisPlusInterceptor plusInterceptor, DataPermissionProperties dataPermissionProperties){

        List<InnerInterceptor> interceptors =new ArrayList<>();
        DataPermissionInterceptor dataPermissionInterceptor = new DataPermissionInterceptor();
        DataPermissionHandler dataPermissionHandler = new DataPermissionHandler(dataPermissionProperties);
        // 添加自定义的数据权限处理器
        dataPermissionInterceptor.setDataPermissionHandler(dataPermissionHandler);
        interceptors.add(dataPermissionInterceptor);

        interceptors.addAll(plusInterceptor.getInterceptors());
        plusInterceptor.setInterceptors(interceptors);
        return new DataPermissionFilter();
    }
}
