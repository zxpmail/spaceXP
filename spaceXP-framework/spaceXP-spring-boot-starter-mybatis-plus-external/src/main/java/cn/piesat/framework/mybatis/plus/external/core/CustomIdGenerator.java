package cn.piesat.framework.mybatis.plus.external.core;

import cn.piesat.framework.mybatis.plus.external.constants.ExternalConstant;
import cn.piesat.framework.mybatis.plus.external.properties.MybatisPlusExternalProperties;
import cn.piesat.framework.redis.core.RedisService;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * <p/>
 * {@code @description}  :自定义生成ID
 * <p/>
 * <b>@create:</b> 2024-06-06 13:44.
 *
 * @author zhouxp
 */
public class CustomIdGenerator implements IdentifierGenerator {
    private final MybatisPlusExternalProperties properties;

    public CustomIdGenerator(MybatisPlusExternalProperties properties) {
        this.properties = properties;
        if (this.properties.getWorkId() > ExternalConstant.WORK_ID_MAX ||this.properties.getWorkId()<ExternalConstant.WORK_ID_MIN)  {
            this.properties.setWorkId(ExternalConstant.WORK_ID_MIN);
        }
        if (this.properties.getLength() < ExternalConstant.KEY_MIN_LENGTH || this.properties.getLength() > ExternalConstant.KEY_MAX_LENGTH) {
            this.properties.setLength(ExternalConstant.KEY_MIN_LENGTH);
        }
    }

    @Resource
    private RedisService redisService;

    @Override
    public Number nextId(Object entity) {
        long num = redisService.increment(properties.getKeyPrefix() +":" + entity.getClass().getName(), getEndTime());
        String id = String.format("%s%02d%0" + properties.getLength() + "d",
                LocalDate.now().format(DateTimeFormatter.ofPattern(ExternalConstant.FMT)),properties.getWorkId(), num);
        return Long.valueOf(id);
    }

    /**
     * 获取当天的结束时间
     */
    public Instant getEndTime() {
        LocalDateTime endTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        return endTime.toInstant(ZoneOffset.ofHours(8));
    }
}
