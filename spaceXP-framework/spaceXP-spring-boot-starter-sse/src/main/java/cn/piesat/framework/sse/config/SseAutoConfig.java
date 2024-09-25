package cn.piesat.framework.sse.config;

import cn.piesat.framework.sse.core.SseClient;
import cn.piesat.framework.sse.properties.SseProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * <p/>
 * {@code @description}: SSE自动配置类
 * <p/>
 * {@code @create}: 2024-09-25 13:59
 * {@code @author}: zhouxp
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({SseProperties.class})
@Slf4j
public class SseAutoConfig {
    /**
     * 默认线程池大小
     */
    private static final int DEFAULT_POOL_SIZE = 10;
    /**
     * 最小线程池大小
     */
    private static final int MIN_POOL_SIZE = 1;
    /**
     * 最大线程池大小
     */
    private static final int MAX_POOL_SIZE = 50;

    @Bean
    public SseClient springWebSocketHandlerInterceptor(SseProperties sseProperties) {
        return new SseClient(sseProperties);
    }

    public int adjustPoolSize(Integer poolSize) {
        if (poolSize == null || poolSize < MIN_POOL_SIZE || poolSize > MAX_POOL_SIZE) {
            log.info("Invalid pool size: " + poolSize + ". Setting default value: " + DEFAULT_POOL_SIZE);
            return DEFAULT_POOL_SIZE;
        }
        return poolSize;
    }

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(SseProperties sseProperties) {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        Integer poolSize = sseProperties.getPoolSize();
        scheduler.setPoolSize(adjustPoolSize(poolSize));
        try {
            scheduler.initialize();
        } catch (Exception e) {
            log.error("Failed to initialize ThreadPoolTaskScheduler", e);
            throw e;
        }

        String threadNamePrefix = "task-" + getClass().getSimpleName();
        scheduler.setThreadNamePrefix(threadNamePrefix);
        return scheduler;
    }
}
