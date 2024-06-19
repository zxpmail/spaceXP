package cn.piesat.framework.websocket.model;

import cn.piesat.framework.websocket.model.enums.MessageEnum;
import lombok.Data;

/**
 * <p/>
 * {@code @description}: 消息类
 * <p/>
 * {@code @create}: 2024-06-19 13:39
 * {@code @author}: zhouxp
 */
@Data
public class MessagePack {
    /**
     * 来源ID
     */
    private String fromId;

    /**
     * 发送ID
     */
    private String toId ;

    /**
     * 类型
     */
    private Integer type = MessageEnum.USER.getType();
    /**
     * 数据
     */
    private Object data;
}
