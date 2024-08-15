package cn.piesat.framework.redis.core;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <p>redis服务操作类</p>
 *
 * @author :zhouxp
 * {@code @date} 2022/9/28 14:27
 * {@code @description} : 包含一系列redis服务操作
 */
@SuppressWarnings(value = {"unchecked", "unused", "UnusedReturnValue"})
@RequiredArgsConstructor
@Slf4j
public class RedisService {
    public final RedisTemplate<String, Object> redisTemplate;

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public <T> void setObject(final String key, final T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void setObject(final String key, final T value, final Long timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    @Nullable
    public Boolean expire(final String key, final long timeout) {
        return redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return true=设置成功；false=设置失败
     */
    @Nullable
    public Boolean expire(final String key, final long timeout, final TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获取有效时间
     *
     * @param key Redis键
     * @return 有效时间
     */
    @Nullable
    public Long getExpire(final String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 判断 key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    @Nullable
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    @Nullable
    public <T> T getObject(final String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除单个对象
     */
    @Nullable
    public Boolean delete(final String key) {
        return redisTemplate.delete(key);
    }

    @Nullable
    public Long deleteAll(final String key) {
        Set<String> keys = redisTemplate.keys(key + "*");
        if (CollectionUtils.isEmpty(keys) || keys.size() < 1) {
            return 0L;
        }
        return redisTemplate.delete(keys);
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     */

    public Boolean deleteObject(final Collection<String> collection) {
        Long delete = redisTemplate.delete(collection);
        if (delete != null) {
            return delete > 0;
        }
        return false;
    }

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> long setList(final String key, final List<T> dataList) {
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return count == null ? 0 : count;
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    @Nullable
    public <T> List<T> getList(final String key) {
        return (List<T>) redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 缓存Set
     *
     * @param key     缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public <T> BoundSetOperations<String, T> setSet(final String key, final Set<T> dataSet) {
        BoundSetOperations<String, T> setOperation = (BoundSetOperations<String, T>) redisTemplate.boundSetOps(key);
        for (T t : dataSet) {
            setOperation.add(t);
        }
        return setOperation;
    }

    /**
     * 添加元素到Redis Set中
     *
     * @param key   Redis键
     * @param value 要添加的值
     */
    public <T> void addSet(String key, T value) {
        redisTemplate.opsForSet().add(key, value);
    }

    /**
     * 删除Set中的元素
     *
     * @param key   Redis键
     * @param value 要删除的值
     */
    public <T> void removeSet(String key, T value) {
        redisTemplate.opsForSet().remove(key, value);
    }

    /**
     * 获得缓存的set
     */
    @Nullable
    public <T> Set<T> getSet(final String key) {
        return (Set<T>) redisTemplate.opsForSet().members(key);
    }

    /**
     * 缓存Map
     */
    public <K,T>  void setMap(final String key, final Map<K, T> dataMap) {
        redisTemplate.opsForHash().putAll(key, dataMap);
    }

    /**
     * 获得缓存的Map
     */
    public <K,T> Map<K, T> getMap(final String key) {
        return (Map<K, T>) redisTemplate.opsForHash().entries(key);
    }

    /**
     * 往Hash中存入数据
     *
     * @param key   Redis键
     * @param hKey  Hash键
     * @param value 值
     */
    public <K,T> void setMapValue(final String key, final K hKey, final T value) {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    @Nullable
    public <K,T> T getMapValue(final String key, final K hKey) {
        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key, hKey);
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key   Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public <T> List<T> getMultiMapValue(final String key, final Collection<Object> hKeys) {
        return (List<T>) redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    /**
     * 删除Hash中的某条数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return 是否成功
     */
    public <K> boolean deleteMapValue(final String key, final K hKey) {
        return redisTemplate.opsForHash().delete(key, hKey) > 0;
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    @Nullable
    public Collection<String> keys(final String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 按通配符删除hash中的key
     *
     * @param hashKey hashKey
     * @param pattern 通配符
     */
    public void deleteMapMatching(String hashKey, String pattern) {
        try (Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(hashKey, ScanOptions.scanOptions().match(pattern).build())) {
            while (cursor.hasNext()) {
                Map.Entry<Object, Object> entry = cursor.next();
                redisTemplate.opsForHash().delete(hashKey, entry.getKey().toString());
            }
        }
    }

    @Value("${space.redis.topics:TOPIC}")
    private String channel;

    private boolean send(Object message) {
        return convertAndSend(channel, message);
    }

    /**
     * 向通道发布消息
     */
    public boolean convertAndSend(String channel, Object message) {
        if (!StringUtils.hasText(channel)) {
            return false;
        }
        try {
            redisTemplate.convertAndSend(channel, message);
            log.info("发送消息成功，channel：{}，message：{}", channel, message);
            return true;
        } catch (Exception e) {
            log.info("发送消息失败，channel：{}，message：{}", channel, message);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取链接工厂
     */
    public RedisConnectionFactory getConnectionFactory() {
        RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();
        if (connectionFactory == null) {
            throw new IllegalArgumentException("Connection factory cannot be null");
        }
        return redisTemplate.getConnectionFactory();
    }

    /**
     * 自增数
     */
    public long increment(String key) {
        try {
            RedisAtomicLong redisAtomicLong = new RedisAtomicLong(key,getConnectionFactory());
            return redisAtomicLong.incrementAndGet();
        } catch (Exception e) {
            log.error("Error incrementing value: " + e.getMessage());
            throw e;
        }
    }

    /**
     * 自增数（带过期时间）
     */
    public long increment(String key, long time, TimeUnit timeUnit) {
        try {
            RedisAtomicLong redisAtomicLong = new RedisAtomicLong(key, getConnectionFactory());
            redisAtomicLong.expire(time, timeUnit);
            return redisAtomicLong.incrementAndGet();
        } catch (Exception e) {
            log.error("Error incrementing value: " + e.getMessage());
            throw e;
        }
    }

    /**
     * 自增数（带过期时间）
     */
    public long increment(String key, Instant expireAt) {
        try {
            RedisAtomicLong redisAtomicLong = new RedisAtomicLong(key, getConnectionFactory());
            redisAtomicLong.expireAt(expireAt);
            return redisAtomicLong.incrementAndGet();
        } catch (Exception e) {
            log.error("Error incrementing value: " + e.getMessage());
            throw e;
        }
    }

    /**
     * 自增数（带过期时间和步长）
     */
    public long increment(String key, int increment, long time, TimeUnit timeUnit) {
        try {
            RedisAtomicLong redisAtomicLong = new RedisAtomicLong(key, getConnectionFactory());
            redisAtomicLong.expire(time, timeUnit);
            return redisAtomicLong.incrementAndGet();
        } catch (Exception e) {
            log.error("Error incrementing value: " + e.getMessage());
            throw e;
        }
    }

}
