package cn.piesat.framework.netty.util;

import cn.piesat.framework.netty.model.enums.ByteOrderEnum;
import cn.piesat.framework.netty.model.enums.TypeEnum;
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
     * @param byteNum 字节数
     * @param in    数据
     * @param type    枚举类型字符串
     * @param endian  编码
     * @return 解码数据
     */
    public static Object decode(int byteNum, ByteBuf in,
                                String type, ByteOrderEnum endian) {
        Object value;
        final TypeEnum typeEnum = TypeEnum.match(type);
        if (typeEnum == null) {
            throw new IllegalArgumentException("Invalid type: " + type);
        }
        switch (typeEnum) {
            case HEX_STRING:
                value = readHexString(byteNum, in);
                break;
            case USHORT:
                value = readUnSignShort(in, endian);
                break;
            case SHORT:
                value = readShort(in, endian);
                break;
            case INT:
                value = readInt(in, endian);
                break;
            case UINT:
                value = readUnSignInt(in, endian);
                break;
            case BYTE:
                value = readByte(in);
                break;
            case UBYTE:
                value = readUnSignByte(in);
                break;
            default:
                throw new IllegalArgumentException("Unsupported type: " + type);
        }
        return value;
    }

    /**
     * 读无符号byte
     *
     * @param in 编码数据
     * @return 解码数据
     */
    public static short readUnSignByte(ByteBuf in) {
        if (!in.isReadable(1)) {
            throw new IllegalArgumentException("Buffer does not have enough readable bytes.");
        }
        byte by = in.readByte();
        return (short) (by & 0x0FF);
    }

    /**
     * 读byte
     *
     * @param in 编码数据
     * @return 解码数据
     */
    public static byte readByte(ByteBuf in) {
        // 检查是否有足够的字节可以读取
        if (!in.isReadable(1)) {
            throw new IllegalArgumentException("Buffer does not have enough readable bytes.");
        }
        return in.readByte();
    }

    /**
     * 读无符号int
     *
     * @param in   编码数据
     * @param endian 字节序
     * @return 解码数据
     */
    public static long readUnSignInt(ByteBuf in, ByteOrderEnum endian) {
        if (in == null || in.readableBytes() < Integer.BYTES) {
            throw new IllegalArgumentException("ByteBuf is null or too short to read an integer.");
        }
        int intValue = endian == ByteOrderEnum.LITTLE_ENDIAN  ? in.readIntLE() : in.readInt();
        return intValue & 0x0FFFFFFFFL;
    }

    /**
     * 读int
     *
     * @param in   编码数据
     * @param endian 字节序
     * @return 解码数据
     */
    public static int readInt(ByteBuf in, ByteOrderEnum endian) {
        if (in.readableBytes() < Integer.BYTES) {
            throw new IndexOutOfBoundsException("Not enough bytes to read a int value.");
        }
        return endian == ByteOrderEnum.LITTLE_ENDIAN  ? in.readIntLE() : in.readInt();
    }

    /**
     * 读short
     *
     * @param in   编码数据
     * @param endian 字节序
     * @return 解码数据
     */
    public static short readShort(ByteBuf in, ByteOrderEnum endian) {
        if (in.readableBytes() < Short.BYTES) {
            throw new IndexOutOfBoundsException("Not enough bytes to read a short value.");
        }
        return endian == ByteOrderEnum.LITTLE_ENDIAN  ? in.readShortLE() : in.readShort();
    }

    /**
     * 读无符号short
     *
     * @param in   编码数据
     * @param endian 字节序
     * @return 解码数据
     */
    public static int readUnSignShort(ByteBuf in, ByteOrderEnum endian) {
        if (in.readableBytes() < Short.BYTES) {
            throw new IndexOutOfBoundsException("Not enough bytes to read a short value.");
        }
        short shortValue = endian == ByteOrderEnum.LITTLE_ENDIAN ? in.readShortLE() : in.readShort();
        return shortValue & 0x0FFFF;
    }

    /**
     * 读Hex字符串
     *
     * @param num  字节长度
     * @param in 编码数据
     * @return 字符串
     */
    public static String readHexString(int num, ByteBuf in) {
        String value = ByteBufUtil.hexDump(in, 0, num);
        readByteBuf(num, in);
        return value;
    }

    /**
     * 读Hex字符串没有数据缓冲区偏移
     *
     * @param num  字节长度
     * @param in 编码数据
     * @return 字符串
     */
    public static String readHexStringWithoutOffset(int num, ByteBuf in) {
        if (in == null) {
            throw new IllegalArgumentException("ByteBuf cannot be null");
        }
        int readableBytes = in.readableBytes();
        if (num < 0 || num > readableBytes) {
            throw new IllegalArgumentException("Invalid num: " + num + ". Should be between 0 and " + readableBytes);
        }
        return ByteBufUtil.hexDump(in, 0, num);
    }

    /**
     * 移动读指针
     *
     * @param num  移动字节数
     * @param in 数据缓冲区ByteBuf
     */
    private static void readByteBuf(int num, ByteBuf in) {
        // 防止空指针异常
        if (in == null) {
            throw new IllegalArgumentException("ByteBuf cannot be null");
        }

        // 确保num的合法性
        if (num < 1) {
            throw new IllegalArgumentException("Number of bytes to read must be at least 1");
        }

        // 避免超出Buffer长度的读取
        if (num > in.readableBytes()) {
            throw new IndexOutOfBoundsException("Not enough readable bytes in buffer");
        }
        // 统一读取逻辑
        if (num == 1) {
            in.readByte();
        } else {
            in.readBytes(num);
        }
    }
}
