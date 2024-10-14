package cn.piesat.framework.netty.util;

import cn.piesat.framework.netty.model.enums.ByteOrderEnum;
import cn.piesat.framework.netty.properties.NettyProperties;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Set;

/**
 * <p/>
 * {@code @description}: 消息工具类
 * <p/>
 * {@code @create}: 2024-10-14 15:46
 * {@code @author}: zhouxp
 */
public class MessageUtils {
    /**
     * 注意第一位版本数据
     * @param in 待解析ByteBuf
     * @param items 解析数据项目
     * @param version 版本值
     * @param byteOrderEnum 字节顺序 大端还是小端
     * @param packetSize 数据包长度
     * @return 已经解析数据
     */
    public static HashMap<String,Object> byteBuf2Message(ByteBuf in, Set<NettyProperties.DataItem> items,
                                                         String version,ByteOrderEnum byteOrderEnum, Integer packetSize){

        return null;
    }
}
