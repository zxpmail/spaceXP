package cn.piesat.framework.common.pipeline;

import cn.piesat.framework.common.pipeline.context.ChannelHandlerContext;
import cn.piesat.framework.common.pipeline.utils.ChannelContextUtil;

/**
 * <p/>
 * {@code @description}: 管道执行器
 * <p/>
 * {@code @create}: 2024-11-14 13:18
 * {@code @author}: zhouxp
 */
public  class ChannelPipelineExecutor {
    public static ChannelPipeline pipeline(){
        ChannelHandlerContext ctx = new ChannelHandlerContext();
        ChannelPipeline channelPipeline = new ChannelPipeline();
        ChannelContextUtil.set(ctx);
        return channelPipeline;
    }
}
