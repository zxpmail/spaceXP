package cn.piesat.framework.redis.model;

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
public class MessageBody<T> implements Serializable {
    /**
     * 来源ID
     */
    private T fromId;

    /**
     * 发送ID
     */
    private T toId;

    /**
     * 类型
     */
    private Integer type;
    /**
     * 数据
     */
    private Object data;

}
