package cn.piesat.tests.redis.controller;

import cn.piesat.framework.redis.core.RedisMessageService;

import cn.piesat.framework.common.model.entity.MessageEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2023/11/22 17:09.
 *
 * @author zhouxp
 */
@Service
@Slf4j
public class MessageServiceImpl implements RedisMessageService {
    @Override
    public void handle(MessageEntity messageEntity) {
        RedisMessageService.super.handle(messageEntity);
        log.info("收到消息: {}, 类型: {} 消息标题:{}",messageEntity.getBody(),messageEntity.getType(),messageEntity.getTitle());
    }
}
