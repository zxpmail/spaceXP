package cn.piesat.test.common.handler;




import cn.piesat.framework.common.pipeline.context.ChannelHandlerContext;
import cn.piesat.framework.common.pipeline.handler.ChannelBoundHandler;
import cn.piesat.framework.common.pipeline.model.ChannelHandlerRequest;
import cn.piesat.test.common.model.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserPrintChannelHandler implements ChannelBoundHandler {
    @Override
    public boolean handler(ChannelHandlerContext chx) {
        ChannelHandlerRequest<?> channelHandlerRequest = chx.getChannelHandlerRequest();
        log.info("------------------------------------步骤四：打印用户数据【"+channelHandlerRequest.getId()+"】");
        Object params = channelHandlerRequest.getParams();
        if(params instanceof User){

            log.info("user {}",params);
        }

        return false;
    }
}
