package cn.piesat.tests.mybaits.plus.config;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2024-05-21 9:25.
 *
 * @author zhouxp
 */
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.RequestContextListener;

@Configuration
public class AsyncConfig {

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("AsyncThread-");
        executor.setTaskDecorator(new RequestContextCopyingTaskDecorator());
        executor.initialize();
        return executor;
    }

    // 注册RequestContextListener确保RequestContext在每个请求开始时被初始化
    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }
}