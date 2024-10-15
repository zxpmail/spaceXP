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
     * @param properties    解析数据项目
     * @param byteOrderEnum 字节顺序 大端还是小端
     * @return 已经解析数据
     */
    public static HashMap<String, Object> byteBuf2Map(ByteBuf in, NettyProperties properties,
                                                      ByteOrderEnum byteOrderEnum, ErrorLogService errorLogService) {

        if (in.readableBytes() < properties.getPacketSize()) {
            return null;
        }
        long version = (long) DecodeUtils.decode(0, in, properties.getVersionType(), byteOrderEnum);
        int beginIdx = 0; //记录包头位置
        int num = 0;
        while (version != properties.getVersionValue()) {
            // 获取包头开始的index
            beginIdx = in.readerIndex();
            // 标记包头开始的index
            in.markReaderIndex();
            // 读到了协议的开始标志，结束while循环
            version = (long) DecodeUtils.decode(0, in, properties.getVersionType(), byteOrderEnum);
            // 未读到包头，略过一个字节
            // 每次略过，一个字节，去读取，包头信息的开始标记
            in.resetReaderIndex();
            in.readByte();
            // 当略过，一个字节之后，
            // 数据包的长度，又变得不满足
            // 此时，应该结束。等待后面的数据到达
            if (in.readableBytes() < properties.getPacketSize() - 1) {
                byte[] bytes = new byte[num];
                in.getBytes(0, bytes);
                errorLogService.send(bytes);
                return null;
            }
            num++;
        }

        //剩余长度不足可读取数量[没有内容长度位]
        if (in.readableBytes() < properties.getPacketSize() - properties.getVersionBytes()) {
            in.readerIndex(beginIdx);
            return null;
        }
        HashMap<String, Object> map = new HashMap<>();
        for (NettyProperties.DataItem item : properties.getItems()) {
            if (item.getIsKipped()) {
                in.readBytes(item.getBytes());
            } else {
                Object decode = DecodeUtils.decode(item.getBytes(), in, item.getType(), byteOrderEnum);
                if (item.getIsPackageLength()) {
                    int len = (int) decode;
                    if (len > properties.getMaxPacketSize()) {
                        byte[] bytes = new byte[properties.getPacketSize()];
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

    public static void Map2byteBuf(ByteBuf out, NettyProperties properties,
                                   ByteOrderEnum byteOrderEnum, Map<String, Object> data) {
        Object version = data.get("version");
        EncodeUtils.encode(properties.getVersionType(), version, out, byteOrderEnum);
        for (NettyProperties.DataItem item : properties.getItems()) {
            if (item.getIsKipped()) {
                out.writeZero(item.getBytes());
            } else {

            }
        }
    }

}
