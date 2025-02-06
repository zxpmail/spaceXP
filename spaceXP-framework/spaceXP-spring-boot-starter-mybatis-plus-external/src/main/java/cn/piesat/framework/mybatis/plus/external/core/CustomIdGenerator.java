package cn.piesat.framework.mybatis.plus.external.core;

import cn.piesat.framework.mybatis.plus.external.constants.ExternalConstant;
import cn.piesat.framework.mybatis.plus.external.properties.MybatisPlusExternalProperties;
import cn.piesat.framework.redis.core.RedisService;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.toolkit.Sequence;
import io.lettuce.core.RedisConnectionException;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class CustomIdGenerator implements IdentifierGenerator {
    private final MybatisPlusExternalProperties properties;
    private final Sequence sequence;


    public CustomIdGenerator(MybatisPlusExternalProperties properties) {
        this.properties = properties;
        if (this.properties.getWorkId() > ExternalConstant.WORK_ID_MAX || this.properties.getWorkId() < ExternalConstant.WORK_ID_MIN) {
            this.properties.setWorkId(ExternalConstant.WORK_ID_MIN);

        }
        if (this.properties.getLength() < ExternalConstant.KEY_MIN_LENGTH || this.properties.getLength() > ExternalConstant.KEY_MAX_LENGTH) {
            this.properties.setLength(ExternalConstant.KEY_MIN_LENGTH);
        }
        this.sequence = new Sequence(this.properties.getWorkId(), this.properties.getLength());
    }

    @Resource
    private RedisService redisService;

    @Override
    public synchronized Number nextId(Object entity) {
        Number id;
        try {
            long num = redisService.increment(properties.getKeyPrefix() + ":" + entity.getClass().getName(), getEndTime());
            String sid = String.format("%s%02d%0" + properties.getLength() + "d",
                    LocalDate.now().format(DateTimeFormatter.ofPattern(ExternalConstant.FMT)), properties.getWorkId(), num);
            id = Long.valueOf(sid);
        }  catch (RedisConnectionException e) {
            id = sequence.nextId();
            log.error("redis connect error! id:{}", id);
        } catch (NumberFormatException e){
            log.error("redis number error ", e);
            id = sequence.nextId();
        }
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
