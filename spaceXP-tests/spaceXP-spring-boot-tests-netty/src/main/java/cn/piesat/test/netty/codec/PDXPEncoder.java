package cn.piesat.test.netty.codec;

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
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Map<String, Object> stringObjectMap, ByteBuf byteBuf) throws Exception {

    }
}
