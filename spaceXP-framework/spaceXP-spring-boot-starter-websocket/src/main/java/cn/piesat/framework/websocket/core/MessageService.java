package cn.piesat.framework.websocket.core;

import cn.piesat.framework.websocket.model.MessagePack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p/>
 * {@code @description}: 用户组信息接口
 * <p/>
 * {@code @create}: 2024-06-19 10:56
 * {@code @author}: zhouxp
 */
public interface MessageService {
    Logger log =  LoggerFactory.getLogger(MessageService.class);
    default   void addUser2Group(String userId){
        log.info("Adding a userId {} to a group" ,userId);
    }

    default  void handleTextMessage(MessagePack  messagePack){
        log.info("message: {} " ,messagePack);
    }
}
