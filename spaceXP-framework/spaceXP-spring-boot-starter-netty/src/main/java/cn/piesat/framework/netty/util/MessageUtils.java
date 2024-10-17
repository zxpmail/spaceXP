package cn.piesat.framework.netty.util;

import cn.piesat.framework.netty.core.ErrorLogService;
import cn.piesat.framework.netty.model.enums.ByteOrderEnum;
import cn.piesat.framework.netty.properties.NettyProperties;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * {@code @description}: 消息工具类
 * <p/>
 * {@code @create}: 2024-10-14 15:46
 * {@code @author}: zhouxp
 */
@Slf4j
public class MessageUtils {
    /**
     * 注意第一位版本数据
     *
     * @param in            待解析ByteBuf
     * @param messageItem    解析数据项目
     * @param byteOrderEnum 字节顺序 大端还是小端
     * @return 已经解析数据
     */
    public static HashMap<String, Object> byteBuf2Map(ByteBuf in, NettyProperties.MessageItem messageItem,
                                                      ByteOrderEnum byteOrderEnum, ErrorLogService errorLogService) {

        if (in.readableBytes() < messageItem.getHeaderPacketSize()) {
            return null;
        }
        Long version = 0L;
        Object ver = DecodeUtils.decode(0, in, messageItem.getVersionType(), byteOrderEnum);
        if(ver instanceof Short){
            version = Long.valueOf((Short) ver);
        }else if(ver instanceof  Integer){
            version = Long.valueOf((Integer) ver);
        }else if(ver instanceof Long){
            version = (Long) ver;
        }else if(ver instanceof Byte){
            version =Long.valueOf((Byte) ver);
        }else{
            log.error("version info error {} .........",ver);
            in.markReaderIndex();
            return  null;
        }
        int beginIdx = 0; //记录包头位置
        int num = 0;
        while (version != messageItem.getVersionValue()) {
            // 获取包头开始的index
            beginIdx = in.readerIndex();
            // 标记包头开始的index
            in.markReaderIndex();
            // 读到了协议的开始标志，结束while循环
            version = (long) DecodeUtils.decode(0, in, messageItem.getVersionType(), byteOrderEnum);
            // 未读到包头，略过一个字节
            // 每次略过，一个字节，去读取，包头信息的开始标记
            in.resetReaderIndex();
            in.readByte();
            // 当略过，一个字节之后，
            // 数据包的长度，又变得不满足
            // 此时，应该结束。等待后面的数据到达
            if (in.readableBytes() < messageItem.getHeaderPacketSize() - 1) {
                byte[] bytes = new byte[num];
                in.getBytes(0, bytes);
                errorLogService.send(bytes);
                return null;
            }
            num++;
        }

        //剩余长度不足可读取数量[没有内容长度位]
        if (in.readableBytes() < messageItem.getHeaderPacketSize() - messageItem.getVersionBytes()) {
            in.readerIndex(beginIdx);
            return null;
        }
        HashMap<String, Object> map = new HashMap<>();
        for (NettyProperties.DataItem item : messageItem.getItems()) {
            if (item.getIsKipped()) {
                in.readBytes(item.getBytes());
            } else {
                Object decode = DecodeUtils.decode(item.getBytes(), in, item.getType(), byteOrderEnum);
                if (item.getIsPackageLength()) {
                    int len = (int) decode;
                    if (len > messageItem.getMaxPacketSize()) {
                        byte[] bytes = new byte[messageItem.getHeaderPacketSize()];
                        in.getBytes(0, bytes);
                        errorLogService.send(bytes);
                        in.markReaderIndex();
                        return null;
                    } else {
                        byte[] data = new byte[len];
                        try {
                            in.readBytes(data);
                            map.put("data", data);
                        } catch (Exception e) {
                            // 处理读取异常
                            log.error("Error reading bytes: {}", e.getMessage());
                            return null;
                        }
                    }
                } else {
                    map.put(item.getName(), decode);
                }
            }
        }
        return map;
    }

    /**
     * 从NettyProperties.DataItem中顺序读取map值写入ByteBuf中
     *
     * @param out           写入的ByteBuf
     * @param messageItem    配置属性
     * @param byteOrderEnum 字节顺序
     * @param data          需要写入的数据
     */
    public static void Map2byteBuf(ByteBuf out, NettyProperties.MessageItem messageItem,
                                   ByteOrderEnum byteOrderEnum, Map<String, Object> data) {
        Object version = data.get("version");
        if (version == null) {
            log.error("version data is null");
            return;
        }
        EncodeUtils.encode(messageItem.getVersionType(), version, out, byteOrderEnum);
        for (NettyProperties.DataItem item : messageItem.getItems()) {
            if (item.getIsKipped()) {
                out.writeZero(item.getBytes());
            } else {
                if (item.getIsPackageLength()) {
                    Object o = data.get("data");
                    if (o instanceof byte[]) {
                        byte[] bytes = (byte[]) o;
                        int length = bytes.length;
                        EncodeUtils.encode(item.getType(), length, out, byteOrderEnum);
                        out.writeBytes(bytes);
                    } else {
                        log.error("user data is null");
                        throw new IllegalArgumentException("Expected byte[] for 'data' key");
                    }
                } else {
                    Object o = data.get(item.getName());
                    if (o == null) {
                        log.error("item data {} is null ", item.getName());
                        return;
                    }
                    EncodeUtils.encode(item.getType(), o, out, byteOrderEnum);
                }
            }
        }
    }
}
