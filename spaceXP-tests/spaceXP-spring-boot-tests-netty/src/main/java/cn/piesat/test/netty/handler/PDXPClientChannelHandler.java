package cn.piesat.test.netty.handler;

import cn.piesat.test.netty.client.TcpClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Map;

/**
 * <p/>
 * {@code @description}: pdxp处理器
 * <p/>
 * {@code @create}: 2024-10-16 15:40
 * {@code @author}: zhouxp
 */
public class PDXPClientChannelHandler extends SimpleChannelInboundHandler<Map<String,Object>> {

    private final TcpClient tcpClient;

    public PDXPClientChannelHandler(TcpClient tcpClient) {
        this.tcpClient = tcpClient;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Map<String, Object> stringObjectMap) throws Exception {

    }
}
