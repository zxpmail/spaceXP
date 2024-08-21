package cn.piesat.framework.common.model.entity;

import cn.piesat.framework.common.model.enums.MessageEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p/>
 * {@code @description}  :redis消息体
 * <p/>
 * <b>@create:</b> 2023/11/22 15:58.
 *
 * @author zhouxp
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageEntity implements Serializable {
    /**
     * 应用ID
     */
    private Integer appId = 0;
    /**
     * 来源ID
     */
    private Long fromId;

    /**
     * 发送ID
     */
    private Long toId;

    /**
     * 类型
     */
    private Integer type = MessageEnum.USER.getType();

    /**
     * 消息标题
     */
    private String title;
    /**
     * 消息体
     */
    private Object body;

    public MessageEntity(Long fromId, Long toId, Integer type, String title, Object body) {
        this.appId = 0;
        this.fromId = fromId;
        this.toId = toId;
        this.type = type;
        this.title = title;
        this.body = body;
    }
}
