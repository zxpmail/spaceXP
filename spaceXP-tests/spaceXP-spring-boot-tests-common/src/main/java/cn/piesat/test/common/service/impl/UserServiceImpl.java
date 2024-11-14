package cn.piesat.test.common.service.impl;




import cn.piesat.framework.common.pipeline.ChannelPipelineExecutor;
import cn.piesat.framework.common.pipeline.model.ChannelHandlerRequest;
import cn.piesat.test.common.handler.UserCheckChannelHandler;
import cn.piesat.test.common.handler.UserFillUsernameAndEmailChannelHandler;

import cn.piesat.test.common.handler.UserPrintChannelHandler;
import cn.piesat.test.common.handler.UserPwdEncryptChannelHandler;
import cn.piesat.test.common.model.User;
import cn.piesat.test.common.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public boolean save(User user) {
       return ChannelPipelineExecutor.pipeline()
                .addLast(new UserCheckChannelHandler())
                .addLast(new UserFillUsernameAndEmailChannelHandler())
                .addLast(new UserPwdEncryptChannelHandler())
                .addLast(new UserPrintChannelHandler())
                .start(new ChannelHandlerRequest<>().setParams(user));
    }
}
