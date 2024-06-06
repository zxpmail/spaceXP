package cn.piesat.tests.redis.controller;

import cn.piesat.framework.redis.core.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2024-06-05 17:23.
 *
 * @author zhouxp
 */
@Service
public class IdGeneratorService {
    @Autowired
    RedisService redisService;

    /**
     * 生成id（每日重置自增序列）
     * 格式：日期 + 6位自增数
     * 如：20210804000001
     * @param key
     * @param length
     * @return
     */
    public String generateId(String key, Integer length) {
        long num = redisService.increment(key, getEndTime());
        String id = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + String.format("%0" + length + "d", num);
        return id;
    }

    /**
     * 获取当天的结束时间
     */
    public Instant getEndTime() {
        LocalDateTime endTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        return endTime.toInstant(ZoneOffset.ofHours(8));
    }
}
