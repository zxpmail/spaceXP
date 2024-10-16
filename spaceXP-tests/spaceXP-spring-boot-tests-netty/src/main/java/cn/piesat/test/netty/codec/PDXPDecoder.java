package cn.piesat.test.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * <p/>
 * {@code @description}:PDXP协议解码器
 * <p/>
 * {@code @create}: 2024-10-16 15:34
 * {@code @author}: zhouxp
 */
public class PDXPDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

    }
}
