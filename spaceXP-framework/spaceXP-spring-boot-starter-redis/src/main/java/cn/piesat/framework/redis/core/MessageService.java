package cn.piesat.framework.redis.core;

import cn.piesat.framework.redis.model.MessageBody;

/**
 * <p/>
 * {@code @description}  :消息处理服务
 * <p/>
 * <b>@create:</b> 2023/11/22 16:02.
 *
 * @author zhouxp
 */
public interface MessageService<T> {
    default void handle(MessageBody<T> messageBody) {
    }
}
