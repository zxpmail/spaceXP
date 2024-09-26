package cn.piesat.tests.sse.service.impl;

import cn.piesat.framework.common.model.entity.MessageEntity;
import cn.piesat.framework.redis.core.RedisMessageService;
import cn.piesat.framework.sse.core.SseClient;
import cn.piesat.framework.sse.util.SseSessionHolder;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;


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
        if (ObjectUtils.isEmpty(messageEntity)){
            return;
        }
        Integer appId = messageEntity.getAppId();
        Long toId = messageEntity.getToId();
        SseEmitter session = SseSessionHolder.getSession(String.valueOf(toId), String.valueOf(appId));
        String s = JSON.toJSONString(messageEntity);
        sseClient.sendMessage(String.valueOf(toId), String.valueOf(appId),String.valueOf(s.hashCode()),s);
        log.info("收到消息: {}, 类型: {} 消息标题:{}",messageEntity.getBody(),messageEntity.getType(),messageEntity.getTitle());
    }
}
