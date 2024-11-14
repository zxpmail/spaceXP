package cn.piesat.framework.common.pipeline.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p/>
 * {@code @description}: 链路上行文处理请求
 * <p/>
 * {@code @create}: 2024-11-14 10:33
 * {@code @author}: zhouxp
 */
@Data
@Accessors(chain = true)
public class ChannelHandlerRequest<T> {

    private String id;

    private T params;

}
