package cn.piesat.test.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * {@code @description}: 心跳处理器
 * <p/>
 * {@code @create}: 2024-10-17 15:02
 * {@code @author}: zhouxp
 */
public class HeartbeatHandler extends ChannelInboundHandlerAdapter {

    private final Map<String,Object> map = new HashMap<>();

    public HeartbeatHandler() {
        map.put("mid",10);
        map.put("sid",10);
        map.put("did",10);
        map.put("bid",10);
        map.put("sn",10);
        map.put("day",10);
        map.put("time",10);
        map.put("data","DE251F1E".getBytes());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            ctx.writeAndFlush(map).addListener(future -> {
                if (!future.isSuccess()) {
                    System.err.println("发送心跳失败");
                }
            });
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
