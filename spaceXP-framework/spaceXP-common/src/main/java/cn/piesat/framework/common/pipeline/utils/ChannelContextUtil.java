package cn.piesat.framework.common.pipeline.utils;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * <p/>
 * {@code @description}: 链路上下文工具类
 * <p/>
 * {@code @create}: 2024-11-13 13:17
 * {@code @author}: zhouxp
 */
public class ChannelContextUtil {
    private static final TransmittableThreadLocal<ChannelContextUtil> CONTEXT = new TransmittableThreadLocal<>() ;

    /**
     * 释放上下文资源
     */
    public void clear() {
        CONTEXT.remove();
    }

    /**
     * 获取当前链路上下文
     */
    public static ChannelContextUtil getCurrentContext() {
        return CONTEXT.get();
    }

    /**
     * 设置上下文
     *
     */
    public static void setContext(ChannelContextUtil channelContext) {
        CONTEXT.set(channelContext);
    }

}
