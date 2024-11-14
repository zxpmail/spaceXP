package cn.piesat.framework.common.pipeline.context;

import cn.piesat.framework.common.pipeline.model.ChannelHandlerRequest;
import lombok.Data;

/**
 * <p/>
 * {@code @description}: 链路上下文
 * <p/>
 * {@code @create}: 2024-11-14 10:22
 * {@code @author}: zhouxp
 */
@Data
public class ChannelHandlerContext{

    private ChannelHandlerRequest<?> channelHandlerRequest;
}
