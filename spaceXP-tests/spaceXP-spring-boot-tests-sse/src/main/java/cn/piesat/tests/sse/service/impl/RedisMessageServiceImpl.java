package cn.piesat.tests.sse.service.impl;

import cn.piesat.framework.common.model.entity.MessageEntity;
import cn.piesat.framework.redis.core.RedisMessageService;
import cn.piesat.framework.sse.core.SseClient;
import cn.piesat.framework.sse.model.SseAttributes;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * <p/>
 * {@code @description}: redis消息处理实现类
 * <p/>
 * {@code @create}: 2024-09-25 16:59
 * {@code @author}: zhouxp
 */
@Service
@Slf4j
public class RedisMessageServiceImpl implements RedisMessageService {
    @Resource
    private SseClient sseClient;

    @Override
    public void handle(MessageEntity messageEntity) {
        if (ObjectUtils.isEmpty(messageEntity)) {
            return;
        }

        Integer appId = messageEntity.getAppId();
        Long toId = messageEntity.getToId();
        ConcurrentHashMap<String, SseAttributes> userMap = sseClient.getUserMap(String.valueOf(toId), String.valueOf(appId));
        if (userMap == null) {
            log.info("属性值： userMap is null");
            return;
        }
        SseAttributes sseAttributes = userMap.get(String.valueOf(appId));
        if (sseAttributes == null) {
            log.info("属性值： sseAttributes is null");
            return;
        }
        Map<String, Object> attributes = sseAttributes.getAttributes();

        log.info("属性值： userId:{},appId: {}", attributes.get("userId"), attributes.get("appId"));
        String s = JSON.toJSONString(messageEntity);
        sseClient.sendMessage(String.valueOf(toId), String.valueOf(appId), String.valueOf(s.hashCode()), s, userMap);
        log.info("收到消息: {}, 类型: {} 消息标题:{}", messageEntity.getBody(), messageEntity.getType(), messageEntity.getTitle());
    }
}
