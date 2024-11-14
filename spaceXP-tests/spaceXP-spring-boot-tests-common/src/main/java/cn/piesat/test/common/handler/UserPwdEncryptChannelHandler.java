package cn.piesat.test.common.handler;



import cn.hutool.crypto.digest.DigestUtil;
import cn.piesat.framework.common.pipeline.context.ChannelHandlerContext;
import cn.piesat.framework.common.pipeline.handler.ChannelBoundHandler;
import cn.piesat.framework.common.pipeline.model.ChannelHandlerRequest;
import cn.piesat.test.common.model.User;


public class UserPwdEncryptChannelHandler implements ChannelBoundHandler {
    @Override
    public boolean handler(ChannelHandlerContext chx) {
        ChannelHandlerRequest<?> channelHandlerRequest = chx.getChannelHandlerRequest();
        System.out.println("------------------------------------步骤三：用户密码明文转密文【"+channelHandlerRequest.getId()+"】");
        Object params = channelHandlerRequest.getParams();
        if(params instanceof User){
            String encryptPwd = DigestUtil.sha256Hex(((User) params).getPassword());
            ((User) params).setPassword(encryptPwd);
            return true;
        }

        return false;
    }
}
