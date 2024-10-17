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
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.err.println("Server connection is closed.");
        tcpClient.start();
        super.channelInactive(ctx);
    }

    public PDXPClientChannelHandler(TcpClient tcpClient) {
        this.tcpClient = tcpClient;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 当发生异常时关闭连接
        cause.printStackTrace();
        ctx.close();
        tcpClient.connect();
    }
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Map<String, Object> map)  {
        System.out.println("Received from server: " + map);
        tcpClient.send(map);
    }
}
