package cn.piesat.test.netty.codec;

import cn.piesat.framework.netty.core.ErrorLogService;
import cn.piesat.framework.netty.model.enums.ByteOrderEnum;
import cn.piesat.framework.netty.properties.NettyProperties;
import cn.piesat.framework.netty.util.MessageUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;

/**
 * <p/>
 * {@code @description}:PDXP协议解码器
 * <p/>
 * {@code @create}: 2024-10-16 15:34
 * {@code @author}: zhouxp
 */
@Slf4j
public class PDXPDecoder extends ByteToMessageDecoder {

    private final NettyProperties.MessageItem messageItem;

    private final ByteOrderEnum byteOrderEnum ;

    public PDXPDecoder(NettyProperties.MessageItem messageItem, ByteOrderEnum byteOrderEnum) {
        this.messageItem = messageItem;
        this.byteOrderEnum = byteOrderEnum;
    }


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list)  {

        HashMap<String, Object> message = MessageUtils.byteBuf2Map(byteBuf, messageItem, byteOrderEnum, new ErrorLogService() {
            @Override
            public void send(byte[] data) {
                ErrorLogService.super.send(data);
                log.info("error ... {}",new String( data));
            }
        });
        if(message == null){
            return;
        }

        list.add(message);
    }
}
