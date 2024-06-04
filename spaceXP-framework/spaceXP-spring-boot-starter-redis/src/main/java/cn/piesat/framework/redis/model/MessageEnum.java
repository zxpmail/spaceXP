package cn.piesat.framework.redis.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <p/>
 * {@code @description}  :消息类别
 * <p/>
 * <b>@create:</b> 2024-06-04 13:50.
 *
 * @author zhouxp
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum MessageEnum {
    HEARTBEAT(0, "心跳"),
    GROUP(1, "群消息"),
    USER(2, "用户消息"),
    OTHER(3, "其他消息");
    private Integer type;
    private String message;

}
