package cn.piesat.framework.log.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p/>
 * {@code @description}  :异步配置
 * <p/>
 * <b>@create:</b> 2023/5/10 9:27.
 *
 * @author zhouxp
 */
@Configuration(proxyBeanMethods = false)
@EnableAsync
public class AsyncExecutorConfig {

    @Value("${spring.task.execution.pool.core-size:4}")
    private int corePoolSize;
    @Value("${spring.task.execution.pool.max-size:40}")
    private int maxPoolSize;
    @Value("${spring.task.execution.pool.queue-capacity:40}")
    private int queueCapacity;
    @Value("${spring.task.execution.thread-name-prefix:execution_}")
    private String namePrefix;
    @Value("${spring.task.execution.pool.keep-alive:1000}")
    private int keepAliveSeconds;
    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        //核心线程数
        executor.setCorePoolSize(corePoolSize);
        //任务队列的大小
        executor.setQueueCapacity(queueCapacity);
        //线程前缀名
        executor.setThreadNamePrefix(namePrefix);
        //线程存活时间
        executor.setKeepAliveSeconds(keepAliveSeconds);

        /**
         * 拒绝处理策略
         * CallerRunsPolicy()：交由调用方线程运行，比如 main 线程。
         * AbortPolicy()：直接抛出异常。
         * DiscardPolicy()：直接丢弃。
         * DiscardOldestPolicy()：丢弃队列中最老的任务。
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        //线程初始化
        executor.initialize();
        return executor;
    }
}
