package cn.piesat.test.common.handler;




import cn.piesat.framework.common.pipeline.context.ChannelHandlerContext;
import cn.piesat.framework.common.pipeline.handler.ChannelBoundHandler;
import cn.piesat.framework.common.pipeline.model.ChannelHandlerRequest;
import cn.piesat.test.common.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class UserCheckChannelHandler implements ChannelBoundHandler {

    
    @Override
    public boolean handler(ChannelHandlerContext chx) {
        ChannelHandlerRequest<?> channelHandlerRequest = chx.getChannelHandlerRequest();
        log.info("------------------------------------步骤一：用户数据校验【"+channelHandlerRequest.getId()+"】");
        Object params = channelHandlerRequest.getParams();
        if(params instanceof User){
            User user = (User)params;
            if(StringUtils.isBlank(user.getFullname())){
                log.error("用户名不能为空");
                return false;
            }
            return true;
        }


        return false;
    }
}
