package cn.piesat.kafka.datasource.strategy;

import cn.piesat.kafka.datasource.model.TestDTO;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2024-03-21 15:19.
 *
 * @author zhouxp
 */
@Slf4j
public class MyStrategy implements RecordFilterStrategy {
    @Override
    public boolean filter(ConsumerRecord consumerRecord) {
        try {
            TestDTO testDto = JSONObject.parseObject(consumerRecord.value().toString(), TestDTO.class);
            if (StringUtils.equals(testDto.getName(), "zhangSan")) {
                return false;
            }
        } catch (Exception e) {
            log.error("filter error {}", consumerRecord.value());
            return true;
        }

        return true;
    }
}
