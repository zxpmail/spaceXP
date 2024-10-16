package cn.piesat.test.netty.codec;

import cn.piesat.framework.netty.model.enums.ByteOrderEnum;
import cn.piesat.framework.netty.properties.NettyProperties;
import cn.piesat.framework.netty.util.MessageUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.Map;

/**
 * <p/>
 * {@code @description}: pdxp编码器
 * <p/>
 * {@code @create}: 2024-10-16 15:37
 * {@code @author}: zhouxp
 */
public class PDXPEncoder extends MessageToByteEncoder<Map<String,Object>> {
    private final NettyProperties.MessageItem messageItem;

    private final ByteOrderEnum byteOrderEnum ;

    public PDXPEncoder(NettyProperties.MessageItem messageItem, ByteOrderEnum byteOrderEnum) {
        this.messageItem = messageItem;
        this.byteOrderEnum = byteOrderEnum;
    }
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Map<String, Object> map, ByteBuf byteBuf) {
        MessageUtils.Map2byteBuf(byteBuf,messageItem,byteOrderEnum,map);
    }
}
