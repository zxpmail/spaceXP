package cn.piesat.tests.redis.controller;

import cn.piesat.framework.redis.core.MessageService;
import cn.piesat.framework.redis.core.RedisService;
import cn.piesat.framework.redis.model.MessageBody;
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
public class MessageServiceImpl implements MessageService {
    @Override
    public void handle(MessageBody messageBody) {
        log.info(messageBody.getData()+","+messageBody.getContent());
    }
}
