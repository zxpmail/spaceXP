package cn.piesat.framework.log.config;

import cn.piesat.framework.log.core.ContextAwarePoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;

import java.util.concurrent.Executor;

/**
 * <p/>
 * {@code @description}  : 异步线程配置
 * <p/>
 * <b>@create:</b> 2023/5/11 9:16.
 *
 * @author zhouxp
 */
@Configuration
public class RequestExecutorConfig extends AsyncConfigurerSupport {
    @Override
    @Bean
    public Executor getAsyncExecutor() {
        return new ContextAwarePoolExecutor();
    }
}
