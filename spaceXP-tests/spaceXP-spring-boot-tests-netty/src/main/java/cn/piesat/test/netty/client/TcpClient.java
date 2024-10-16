package cn.piesat.test.netty.client;

import cn.piesat.framework.netty.properties.NettyProperties;
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

    private final NettyProperties nettyProperties;

    private final EventLoopGroup group;
    private volatile ChannelFuture channelFuture;

    private long reconnectDelayMs ;
    private int retryCount = 0;

    public TcpClient(NettyProperties nettyProperties) {
        this.nettyProperties = nettyProperties;
        this.reconnectDelayMs= nettyProperties.getTcpClient().getReconnectDelayMs();
        this.group = new NioEventLoopGroup();
    }

    public void start() {
        connect();
    }

    public void connect() {
        if (retryCount > 0) {
            // 指定时间间隔后重连
            group.next().schedule(this::doConnect, nettyProperties.getTcpClient().getReconnectDelayMs(), TimeUnit.MILLISECONDS);
        } else {
            // 第一次连接不需要延迟
            doConnect();
        }
    }

    private void doConnect() {
        retryCount++;
        if (retryCount > nettyProperties.getTcpClient().getRetryMaxCount()) {
            // 达到最大重试次数后不再重试
            log.error("Failed to connect after maximum retries............................");
            return;
        }

        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {

                        ch.pipeline().addLast(new PDXPClientChannelHandler(TcpClient.this));
                    }
                });

        channelFuture = b.connect(new InetSocketAddress(nettyProperties.getTcpClient().getServerHost(), nettyProperties.getTcpClient().getServerPort()))
                .addListener((GenericFutureListener<Future<Void>>) future -> {
                    if (future.isSuccess()) {
                        // 连接成功
                        retryCount = 0;
                        reconnectDelayMs = nettyProperties.getTcpClient().getReconnectDelayMs(); // 重置重连延迟时间
                        log.info("Connected to server.........................................");
                    } else {
                        // 连接失败时重连，并增加重连延迟时间
                        log.error("Connection failed, retrying in {} ms.....................................", reconnectDelayMs );
                        reconnectDelayMs *= 2; // 重连间隔翻倍
                        connect();
                    }
                });
    }

    public void send(byte[] message) {
        if (channelFuture != null && channelFuture.channel() != null && channelFuture.channel().isActive()) {
            channelFuture.channel().writeAndFlush(message);
        } else {
            log.error("Channel is not active, cannot send message..........................");
        }
    }

    public void send(Map<String, Object> message) {
        if (channelFuture != null && channelFuture.channel() != null && channelFuture.channel().isActive()) {
            channelFuture.channel().writeAndFlush(message);
        } else {
            log.error("Channel is not active, cannot send message.........................................");
        }
    }

    public void stop() {
        log.info("client is close .........................................");
        group.shutdownGracefully();
    }

}
