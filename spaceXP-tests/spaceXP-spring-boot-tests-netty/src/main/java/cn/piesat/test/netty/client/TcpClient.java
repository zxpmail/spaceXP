package cn.piesat.test.netty.client;

import cn.piesat.framework.netty.model.enums.ByteOrderEnum;
import cn.piesat.framework.netty.properties.NettyProperties;
import cn.piesat.test.netty.codec.PDXPDecoder;
import cn.piesat.test.netty.codec.PDXPEncoder;
import cn.piesat.test.netty.handler.HeartbeatHandler;
import cn.piesat.test.netty.handler.PDXPClientChannelHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
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

    private final EventLoopGroup boss = new NioEventLoopGroup();
    ;
    private final Bootstrap worker = new Bootstrap();
    private volatile ChannelFuture channelFuture;

    private long reconnectDelayMs;
    private int retryCount = 0;

    public TcpClient(NettyProperties nettyProperties) {
        this.nettyProperties = nettyProperties;
        this.reconnectDelayMs = nettyProperties.getTcpClient().getReconnectDelayMs();
        worker.option(ChannelOption.SO_KEEPALIVE, true);
        worker.group(boss)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new PDXPDecoder(nettyProperties.getMessageItem(), ByteOrderEnum.LITTLE_ENDIAN));
                        ch.pipeline().addLast(new PDXPEncoder(nettyProperties.getMessageItem(), ByteOrderEnum.LITTLE_ENDIAN));
                        ch.pipeline().addLast(new IdleStateHandler(0, 0, 5, TimeUnit.SECONDS));
                        ch.pipeline().addLast(new HeartbeatHandler());
                        ch.pipeline().addLast(new PDXPClientChannelHandler(TcpClient.this));
                    }
                });
    }

    public void start() {
        retryCount = 0;
        connect();
    }

    public void connect() {
        if (retryCount > 0) {
            // 指定时间间隔后重连
            boss.next().schedule(this::doConnect, nettyProperties.getTcpClient().getReconnectDelayMs(), TimeUnit.MILLISECONDS);
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
        channelFuture = worker.connect(new InetSocketAddress(nettyProperties.getTcpClient().getServerHost(), nettyProperties.getTcpClient().getServerPort()))
                .addListener((GenericFutureListener<Future<Void>>) future -> {
                    if (future.isSuccess()) {
                        // 连接成功
                        retryCount = 0;
                        reconnectDelayMs = nettyProperties.getTcpClient().getReconnectDelayMs(); // 重置重连延迟时间
                        log.info("Connected to server.........................................");
                    } else {
                        // 连接失败时重连，并增加重连延迟时间
                        log.error("Connection failed, retrying in {} ms.....................................", reconnectDelayMs);
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
        boss.shutdownGracefully();
    }
}
