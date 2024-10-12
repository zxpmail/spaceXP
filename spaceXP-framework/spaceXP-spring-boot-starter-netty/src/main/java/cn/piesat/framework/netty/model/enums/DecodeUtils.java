package cn.piesat.framework.netty.model.enums;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

/**
 * <p/>
 * {@code @description}: 解码工具类
 * <p/>
 * {@code @create}: 2024-10-12 13:13
 * {@code @author}: zhouxp
 */
public final class DecodeUtils {

    private DecodeUtils() {

    }

    /**
     * 解码
     *
     * @param symbol  符号
     * @param byteNum 字节数
     * @param buff    数据
     * @param type    枚举类型字符串
     * @param endian  编码
     * @return 解码数据
     */
    public static Object decode(String symbol, int byteNum, ByteBuf buff,
                                String type, boolean endian) {
        Object value;
        final TypeEnum typeEnum = TypeEnum.match(type);
        if (typeEnum == null) {
            throw new IllegalArgumentException("Invalid type: " + type);
        }

        if (buff.readableBytes() < byteNum) {
            throw new IndexOutOfBoundsException("Insufficient bytes in buffer");
        }
        switch (typeEnum) {
            case HEX_STRING:
                value = readHexString(byteNum, buff);
                break;
            case USHORT:
                value = readUnSignShort(buff, endian);
                break;
            case SHORT:
                value = readShort(buff, endian);
                break;
            case INT:
                value = readInt(buff, endian);
                break;
            case UINT:
                value = readUnSignInt(buff, endian);
                break;
            case BYTE:
                value = readByte(buff);
                break;
            case UBYTE:
                value = readUnSignByte(buff);
                break;
            default:
                throw new IllegalArgumentException("Unsupported type: " + type);
        }

        return value;
    }

    /**
     * 读无符号byte
     *
     * @param buff 编码数据
     * @return 解码数据
     */
    public static short readUnSignByte(ByteBuf buff) {
        if (!buff.isReadable(1)) {
            throw new IllegalArgumentException("Buffer does not have enough readable bytes.");
        }
        byte by = buff.readByte();
        return (short) (by & 0x0FF);
    }

    /**
     * 读byte
     *
     * @param buff 编码数据
     * @return 解码数据
     */
    public static byte readByte(ByteBuf buff) {
        // 检查是否有足够的字节可以读取
        if (!buff.isReadable(1)) {
            throw new IllegalArgumentException("Buffer does not have enough readable bytes.");
        }
        return buff.readByte();
    }

    /**
     * 读无符号int
     *
     * @param buff   编码数据
     * @param endian 字节序
     * @return 解码数据
     */
    public static long readUnSignInt(ByteBuf buff, boolean endian) {
        if (buff == null || buff.readableBytes() < Integer.BYTES) {
            throw new IllegalArgumentException("ByteBuf is null or too short to read an integer.");
        }
        int intValue = endian ? buff.readIntLE() : buff.readInt();
        return intValue & 0x0FFFFFFFFL;
    }

    /**
     * 读int
     *
     * @param buff   编码数据
     * @param endian 字节序
     * @return 解码数据
     */
    public static int readInt(ByteBuf buff, boolean endian) {
        if (buff.readableBytes() < Integer.BYTES) {
            throw new IndexOutOfBoundsException("Not enough bytes to read a int value.");
        }
        return endian ? buff.readIntLE() : buff.readInt();
    }

    /**
     * 读short
     *
     * @param buff   编码数据
     * @param endian 字节序
     * @return 解码数据
     */
    public static short readShort(ByteBuf buff, boolean endian) {
        if (buff.readableBytes() < Short.BYTES) {
            throw new IndexOutOfBoundsException("Not enough bytes to read a short value.");
        }
        return endian ? buff.readShortLE() : buff.readShort();
    }

    /**
     * 读无符号short
     *
     * @param buff   编码数据
     * @param endian 字节序
     * @return 解码数据
     */
    public static int readUnSignShort(ByteBuf buff, boolean endian) {
        if (buff.readableBytes() < Short.BYTES) {
            throw new IndexOutOfBoundsException("Not enough bytes to read a short value.");
        }
        short shortValue = endian ? buff.readShortLE() : buff.readShort();
        return shortValue & 0x0FFFF;
    }

    /**
     * 读Hex字符串
     *
     * @param num  字节长度
     * @param buff 编码数据
     * @return 字符串
     */
    public static String readHexString(int num, ByteBuf buff) {
        String value = ByteBufUtil.hexDump(buff, 0, num);
        readByteBuf(num, buff);
        return value;
    }

    /**
     * 读Hex字符串没有数据缓冲区偏移
     *
     * @param num  字节长度
     * @param buff 编码数据
     * @return 字符串
     */
    public static String readHexStringWithoutOffset(int num, ByteBuf buff) {
        if (buff == null) {
            throw new IllegalArgumentException("ByteBuf cannot be null");
        }
        int readableBytes = buff.readableBytes();
        if (num < 0 || num > readableBytes) {
            throw new IllegalArgumentException("Invalid num: " + num + ". Should be between 0 and " + readableBytes);
        }
        return ByteBufUtil.hexDump(buff, 0, num);
    }

    /**
     * 移动读指针
     *
     * @param num  移动字节数
     * @param buff 数据缓冲区ByteBuf
     */
    private static void readByteBuf(int num, ByteBuf buff) {
        // 防止空指针异常
        if (buff == null) {
            throw new IllegalArgumentException("ByteBuf cannot be null");
        }

        // 确保num的合法性
        if (num < 1) {
            throw new IllegalArgumentException("Number of bytes to read must be at least 1");
        }

        // 避免超出Buffer长度的读取
        if (num > buff.readableBytes()) {
            throw new IndexOutOfBoundsException("Not enough readable bytes in buffer");
        }
        // 统一读取逻辑
        if (num == 1) {
            buff.readByte();
        } else {
            buff.readBytes(num);
        }
    }
}
