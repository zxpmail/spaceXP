package cn.piesat.framework.netty.config;

import cn.piesat.framework.netty.properties.NettyProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p/>
 * {@code @description}: netty配置类
 * <p/>
 * {@code @create}: 2024-10-14 15:14
 * {@code @author}: zhouxp
 */
@Configuration(proxyBeanMethods = false)
public class NettyAutoConfig {
    @Bean
    public NettyProperties getNettyProperties() {
        return new NettyProperties();
    }
}
