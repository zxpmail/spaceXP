package cn.piesat.framework.common.pipeline.handler;

import cn.piesat.framework.common.pipeline.context.ChannelHandlerContext;

/**
 * <p/>
 * {@code @description}: 链路绑定类
 * <p/>
 * {@code @create}: 2024-11-13 14:04
 * {@code @author}: zhouxp
 */
public interface ChannelBoundHandler {
    default boolean handler(ChannelHandlerContext handlerContext){
        return false;
    }
}
