package cn.piesat.test.common.handler;



import cn.piesat.framework.common.pipeline.context.ChannelHandlerContext;
import cn.piesat.framework.common.pipeline.handler.ChannelBoundHandler;

import cn.piesat.framework.common.pipeline.model.ChannelHandlerRequest;
import cn.piesat.test.common.model.User;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;


@Slf4j
public class UserFillUsernameAndEmailChannelHandler implements ChannelBoundHandler {
    @SneakyThrows
    @Override
    public boolean handler(ChannelHandlerContext chx) {
        ChannelHandlerRequest<?> channelHandlerRequest = chx.getChannelHandlerRequest();
        log.info("------------------------------------步骤二：用户名以及邮箱填充【将汉语转成拼音填充】【"+channelHandlerRequest.getId()+"】");
        Object params = channelHandlerRequest.getParams();
        if(params instanceof User){
            User user = (User)params;
            String fullName = user.getFullname();
            HanyuPinyinOutputFormat hanyuPinyinOutputFormat = new HanyuPinyinOutputFormat();
            hanyuPinyinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            String username = PinyinHelper.toHanYuPinyinString(fullName, hanyuPinyinOutputFormat,"_" ,false);
            user.setUsername(username);
            user.setEmail(username + "@qq.com");
            return true;
        }
        return false;
    }
}
