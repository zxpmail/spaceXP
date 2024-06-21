package cn.piesat.test.websocket.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <p/>
 * {@code @description}:
 * <p/>
 * {@code @create}: 2024-06-19 13:43
 * {@code @author}: zhouxp
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
