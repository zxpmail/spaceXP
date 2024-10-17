package cn.piesat.test.netty.config;

import cn.piesat.framework.netty.properties.NettyProperties;
import cn.piesat.test.netty.client.TcpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * <p/>
 * {@code @description}: 配置
 * <p/>
 * {@code @create}: 2024-10-17 16:45
 * {@code @author}: zhouxp
 */
@Configuration
public class AutoConfig {
    @Resource
    private NettyProperties nettyProperties;
    @Bean
    public TcpClient tcpClient(){
        return new TcpClient(nettyProperties);
    }
}
