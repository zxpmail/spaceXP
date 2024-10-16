package cn.piesat.test.netty.client;

import cn.piesat.test.netty.handler.PDXPClientChannelHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p/>
 * {@code @description}: netty客户端
 * <p/>
 * {@code @create}: 2024-10-16 15:43
 * {@code @author}: zhouxp
 */
@Slf4j
public class TcpClient {
    private final String host;
    private final int port;
    private final EventLoopGroup group;
    private volatile ChannelFuture channelFuture;
    private int retryCount = 0;
    private long reconnectDelayMs = 1000; // 初始重连延迟时间（毫秒）

    public TcpClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.group = new NioEventLoopGroup();
    }

    public void start() {
        connect();
    }

    public void connect() {
        if (retryCount > 0) {
            // 指定时间间隔后重连
            group.next().schedule(this::doConnect, reconnectDelayMs, TimeUnit.MILLISECONDS);
        } else {
            // 第一次连接不需要延迟
            doConnect();
        }
    }

    private void doConnect() {
        retryCount++;
        if (retryCount > 5) {
            // 达到最大重试次数后不再重试
            System.err.println("Failed to connect after maximum retries.");
            return;
        }

        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch)  {

                        ch.pipeline().addLast(new PDXPClientChannelHandler(TcpClient.this));
                    }
                });

        channelFuture = b.connect(new InetSocketAddress(host, port))
                .addListener((GenericFutureListener<Future<Void>>) future -> {
                    if (future.isSuccess()) {
                        // 连接成功
                        retryCount = 0;
                        reconnectDelayMs = 1000; // 重置重连延迟时间
                        System.out.println("Connected to server.");
                    } else {
                        // 连接失败时重连，并增加重连延迟时间
                        System.err.println("Connection failed, retrying in " + reconnectDelayMs + "ms...");
                        reconnectDelayMs *= 2; // 重连间隔翻倍
                        connect();
                    }
                });
    }

    public void send(byte[] message) {
        if (channelFuture != null && channelFuture.channel() != null && channelFuture.channel().isActive()) {
            channelFuture.channel().writeAndFlush(message);
        } else {
            System.err.println("Channel is not active, cannot send message.");
        }
    }

    public void send(Map<String,Object> message) {
        if (channelFuture != null && channelFuture.channel() != null && channelFuture.channel().isActive()) {
            channelFuture.channel().writeAndFlush(message);
        } else {
            System.err.println("Channel is not active, cannot send message.");
        }
    }

    public void stop() {
        group.shutdownGracefully();
    }

}
