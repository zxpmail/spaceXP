package cn.piesat.framework.websocket.model;

import lombok.Data;

/**
 * <p/>
 * {@code @description}: 用户信息
 * <p/>
 * {@code @create}: 2024-06-18 15:38
 * {@code @author}: zhouxp
 */
@Data
public class User<T> {
    private T userId;
}
