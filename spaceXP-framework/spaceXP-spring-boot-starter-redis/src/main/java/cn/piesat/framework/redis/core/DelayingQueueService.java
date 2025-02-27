package cn.piesat.framework.redis.core;

import cn.piesat.framework.common.model.vo.ApiResult;
import cn.piesat.framework.redis.bean.RedisQueueMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.CollectionUtils;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p/>
 * {@code @description}: 延迟队列服务
 * <p/>
 * {@code @create}: 2025-02-27 10:12:06
 * {@code @author}: zhouxp
 */
@Slf4j
public class DelayingQueueService implements InitializingBean {

    private final static ObjectMapper MAPPER = Jackson2ObjectMapperBuilder.json().build();

    /**
     * 是否销毁标记 volatile 保证可见性
     **/
    private volatile boolean destroyFlag = false;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ApplicationContext applicationContext;

    // 设定空轮询最大次数
    private static final int SELECTOR_AUTO_REBUILD_THRESHOLD = 512;

    // deadline 以及任务穿插逻辑处理  ，业务处理事件可能是5毫秒
    private final long timeoutMillis = TimeUnit.MILLISECONDS.toNanos(5);

    /**
     * 可以不同业务用不同的key
     */
    @Value("${space.redis.redisQueue.name:test}")
    public String queueName;


    /**
     * 插入消息
     */
    public Boolean push(RedisQueueMessage redisQueueMessage) {
        Boolean addFlag = null;
        try {
            addFlag = stringRedisTemplate.opsForZSet().add(queueName, MAPPER.writeValueAsString(redisQueueMessage), redisQueueMessage.getDelayTime());
        } catch (JsonProcessingException e) {
            log.error("push failed", e);
        }
        return addFlag;
    }

    /**
     * 移除消息
     */
    public Boolean remove(RedisQueueMessage redisQueueMessage) {
        try {
            String messageString = MAPPER.writeValueAsString(redisQueueMessage);
            Long removeCount = stringRedisTemplate.opsForZSet().remove(queueName, messageString);
            return removeCount != null && removeCount > 0;
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize redisQueueMessage: {}", redisQueueMessage, e);
            return false;
        } catch (Exception e) {
            log.error("Failed to remove message from queue {}: {}", queueName, e.getMessage(), e);
            return false;
        }
    }


    /**
     * 拉取最新需要
     * 被消费的消息
     * rangeByScore 根据score范围获取 0-当前时间戳可以拉取当前时间及以前的需要被消费的消息
     */
    public RedisQueueMessage pop() {
        Set<String> strings = stringRedisTemplate.opsForZSet().rangeByScore(queueName, 0, System.currentTimeMillis());
        if (CollectionUtils.isEmpty(strings)) {
            return null;
        }

        List<RedisQueueMessage> msgList = strings.stream()
                .map(msg -> {
                    try {
                        return MAPPER.readValue(msg, RedisQueueMessage.class);
                    } catch (IOException e) {
                        log.error("Error parsing RedisQueueMessage: {}", msg, e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        for (RedisQueueMessage redisQueueMessage : msgList) {
            if (remove(redisQueueMessage)) {
                // 确保 remove 方法是线程安全的
                return redisQueueMessage;
            }
        }
        return null;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        Thread thread = new Thread(() -> {
            int selectCnt = 0;
            while (!Thread.currentThread().isInterrupted() && !destroyFlag) {
                long startTimeNanos = System.nanoTime();
                try {
                    RedisQueueMessage redisQueueMessage = pop();
                    log.info("拉取的数据 {}", redisQueueMessage);
                    if (Objects.nonNull(redisQueueMessage)) {
                        RedisQueueHandleService redisQueueProcessService = adapterHandler(redisQueueMessage.getBeanName());
                        invokeHandler(redisQueueMessage, redisQueueProcessService);
                    }
                    selectCnt++;

                    // 解决空轮询问题
                    long endTimeNanos = System.nanoTime();
                    long elapsedNanos = endTimeNanos - startTimeNanos;
                    log.debug("执行纳秒数 {}", elapsedNanos);

                    if (elapsedNanos >= TimeUnit.MILLISECONDS.toNanos(timeoutMillis)) {
                        // 有效的轮询
                        selectCnt = 1;
                    } else if (SELECTOR_AUTO_REBUILD_THRESHOLD > 0 && selectCnt >= SELECTOR_AUTO_REBUILD_THRESHOLD) {
                        // 如果空轮询次数大于等于SELECTOR_AUTO_REBUILD_THRESHOLD 默认512
                        selectCnt = 1;
                        threadSleep();
                    }
                } catch (Exception e) {
                    log.error("Error processing redisQueueMessage", e);
                }
            }
        }, "loop-redis-queue");

        thread.setDaemon(true);
        thread.start();
    }

    private void invokeHandler(RedisQueueMessage redisQueueMessage, RedisQueueHandleService redisQueueHandleService) {

        try {
            ApiResult<Object> result = redisQueueHandleService.handler(redisQueueMessage);
            ifFailAgainAddQueue(redisQueueMessage, result);
        } catch (Exception e) {
            // 执行出现异常重新加入队列
            push(redisQueueMessage);
            log.error("执行业务代码程序异常",e);
            throw new RuntimeException("执行业务代码异常");
        }
    }

    protected void ifFailAgainAddQueue(RedisQueueMessage redisQueueMessage, ApiResult<Object> result) {
        if (Objects.nonNull(result) && HttpStatus.OK.value() != result.getCode()) {
            // 错误要重新加入队列
            push(redisQueueMessage);
        }
    }


    private RedisQueueHandleService adapterHandler(String beanName) {
        return applicationContext.getBean(beanName, RedisQueueHandleService.class);
    }

    private void threadSleep() {
        try {
            log.info("睡眠了..........");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("睡眠出错..........",e);
        }
    }


    @PreDestroy
    public void destroy() throws Exception {
        log.info("应用程序已关闭..........");
        this.destroyFlag = true;
    }
}
