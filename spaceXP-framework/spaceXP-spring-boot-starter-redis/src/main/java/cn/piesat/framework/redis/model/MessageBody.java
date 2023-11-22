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
public class MessageBody implements Serializable {
    /**
     * 数据
     */
    private String data;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
}
