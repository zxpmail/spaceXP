package cn.piesat.framework.netty.util;

import cn.piesat.framework.netty.model.enums.ByteOrderEnum;
import cn.piesat.framework.netty.model.enums.TypeEnum;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;


/**
 * <p/>
 * {@code @description}: 编码工具类
 * <p/>
 * {@code @create}: 2024-10-12 14:47
 * {@code @author}: zhouxp
 */
@Slf4j
public final class EncodeUtils {

    private EncodeUtils() {
    }

    /**
     * 组装配置文件所配置的ByteBuf
     *
     * @param type          枚举类型字符串
     * @param value         值
     * @param out           ByteBuf缓存域
     * @param byteOrderEnum 字节序
     */
    public static void encode(String type, Object value,
                              ByteBuf out, ByteOrderEnum byteOrderEnum) {
        if (type == null || value == null) {
            throw new IllegalArgumentException("Arguments 'type' and 'value' cannot be null");
        }
        final TypeEnum typeEnum = TypeEnum.match(type);
        switch (typeEnum) {
            // 有符号int
            case INT:
                writeInt(value, out, byteOrderEnum);
                break;
            case UINT:
                writeUnSignInt(value, out, byteOrderEnum);
                break;
            case SHORT:
                writeShort(value, out, byteOrderEnum);
                break;
            case USHORT:
                writeUnSignShort(value, out, byteOrderEnum);
                break;
            case BYTE:
                writeByte(value, out);
                break;
            case UBYTE:
                writeUnSignByte(value, out);
                break;
            case HEX_STRING:
                writeHexString(value, out);
                break;
            default:
                throw new IllegalArgumentException("Unsupported type: " + typeEnum);
        }

    }

    /**
     * 写字符串数据
     *
     * @param value 值
     * @param out   ByteBuf缓存区
     */
    public static void writeHexString(Object value, ByteBuf out) {
        if (value == null) {
            throw new IllegalArgumentException("The provided object is null");
        }
        if (!(value instanceof String)) {
            throw new IllegalArgumentException("The provided object must be of type String");
        }
        try {
            String m = (String) value;
            writeHexString(m, out);
        } catch (Exception e) {
            log.error("write hexString {} error .......", value, e);
            throw e;
        }

    }

    /**
     * 写字符串数据
     *
     * @param value 值
     * @param out   ByteBuf缓存区
     */
    public static void writeHexString(String value, ByteBuf out) {
        // 检查参数是否为null
        if (value == null || out == null) {
            throw new IllegalArgumentException("Parameters 'value' and 'out' must not be null.");
        }
        try {
            final byte[] bytes = ByteBufUtil.decodeHexDump(value);
            out.writeBytes(bytes);
        } catch (IllegalArgumentException e) {
            // 记录异常，给出提示信息
            log.error("Failed to decode hex string: {} ", e.getMessage());
            throw e;
        }
    }

    /**
     * 写字符串数据
     *
     * @param value 值
     * @param out   ByteBuf缓存区
     */
    public static void writeString(Object value, ByteBuf out) {
        // 检查参数是否为null
        if (value == null || out == null) {
            throw new IllegalArgumentException("Parameters 'value' and 'out' must not be null.");
        }
        if (!(value instanceof String)) {
            throw new IllegalArgumentException("The provided object must be of type String");
        }
        try {
            String m = (String) value;
            writeString(m, out);
        } catch (Exception e) {
            log.error("write String {} error .......", value, e);
            throw e;
        }
    }

    /**
     * 写字符串数据
     *
     * @param value 值
     * @param out   ByteBuf缓存区
     */
    public static void writeString(String value, ByteBuf out) {
        if (value == null || out == null) {
            throw new IllegalArgumentException("Parameters 'value' and 'out' must not be null.");
        }
        byte[] bytes = value.getBytes();
        out.writeBytes(bytes);
    }

    /**
     * 写int数据
     *
     * @param value         值
     * @param out           ByteBuf缓存区
     * @param byteOrderEnum 字节序
     */
    public static void writeInt(Object value, ByteBuf out, ByteOrderEnum byteOrderEnum) {
        // 检查obj是否为null
        if (value == null) {
            throw new IllegalArgumentException("Object cannot be null.");
        }
        if (!(value instanceof Integer)) {
            throw new IllegalArgumentException("The provided object must be of type Integer");
        }
        try {
            int m = (int) value;

            //小字节序
            if (byteOrderEnum == ByteOrderEnum.LITTLE_ENDIAN) {
                out.writeIntLE(m);
            } else {
                out.writeInt(m);
            }
        } catch (Exception e) {
            log.error("write int {} error .......", value, e);
            throw e;
        }

    }

    /**
     * 写无符号byte数据
     *
     * @param value 值
     * @param out   ByteBuf缓存区
     */
    public static void writeUnSignByte(Object value, ByteBuf out) {
        short m;
        if (value instanceof Long) {
            long m1 = (Long) value;
            m = (short) m1;
        } else if (value instanceof Integer) {
            int m1 = (Integer) value;
            m = (short) m1;
        } else if (value instanceof Short) {
            m = (short) value;
        } else {
            log.error("data cast error {}", value);
            return;
        }
        writeUnSignByte(m, out);
    }

    /**
     * 写无符号byte数据
     *
     * @param m   值
     * @param out ByteBuf缓存区
     */
    public static void writeUnSignByte(short m, ByteBuf out) {
        writeUnSignByteBase(m, out);
    }

    /**
     * 写byte数据
     *
     * @param value 值
     * @param out   ByteBuf缓存区
     */
    public static void writeByte(Object value, ByteBuf out) {
        try {
            short m = (short) value;
            assert m <= 127 && m >= -128;
            out.writeByte(m);
        } catch (Exception e) {
            log.error("write byte {} error .......", value, e);
            throw e;
        }
    }

    /**
     * 写无符号short数据
     *
     * @param value         值
     * @param out           ByteBuf缓存区
     * @param byteOrderEnum 字节序
     */
    public static void writeUnSignShort(Object value, ByteBuf out, ByteOrderEnum byteOrderEnum) {
        try {
            int m = (int) value;
            assert m >= 0 && m <= 65535;
            m &= 0x0FFFF;
            writeShort(m, out, byteOrderEnum);
        } catch (Exception e) {
            log.error("write SignShort {} error .......", value, e);
            throw e;
        }
    }

    /**
     * 写short数据
     *
     * @param value         值
     * @param out           ByteBuf缓存区
     * @param byteOrderEnum 字节序
     */
    public static void writeShort(Object value, ByteBuf out, ByteOrderEnum byteOrderEnum) {
        try {
            int m = (int) value;
            //-32768~32767
            assert m >= -32768 && m <= 32767;
            writeShort(m, out, byteOrderEnum);
        } catch (Exception e) {
            log.error("write Short {} error .......", value, e);
            throw e;
        }
    }

    public static void writeUnSignInt(Object value, ByteBuf out, ByteOrderEnum byteOrderEnum) {
        try {
            long m = (Long) value;
            writeUnSignInt(out, m, byteOrderEnum);
        } catch (Exception e) {
            log.error("write SignInt {} error .......", value, e);
            throw e;
        }
    }

    public static void writeUnSignInt(ByteBuf out, Long value, ByteOrderEnum byteOrderEnum) {
        if (value < 0 || value >= 0x100000000L) {
            throw new IllegalArgumentException("Value must be between 0 and 0xFFFFFFFF");
        }
        String hexString = Long.toHexString(value);
        hexString = fullFillHexString(hexString);
        final byte[] bytes = hexEncodeBytes(hexString, 8);  // 8 bits in a byte, 4 bytes total

        try {
            if (byteOrderEnum == ByteOrderEnum.LITTLE_ENDIAN) {
                final byte[] littleBytes = {bytes[3], bytes[2], bytes[1], bytes[0]};
                out.writeBytes(littleBytes);
            } else {
                out.writeBytes(bytes);
            }
        } catch (Exception e) {
            log.error("Error writing bytes: {} ", e.getMessage());
            throw e;
        }
    }

    public static byte[] hexEncodeBytes(String hexString, int index) {
        // 参数校验
        if (index <= 0) {
            throw new IllegalArgumentException("Index must be greater than 0.");
        }

        final byte[] bytes = ByteBufUtil.decodeHexDump(hexString);
        int len = bytes.length;

        // 创建目标数组
        byte[] bytesTmp = new byte[index];

        // 如果原始字节数组长度小于目标长度，则用 0 填充剩余部分
        if (len < index) {
            Arrays.fill(bytesTmp, 0, index - len, (byte) 0x00);
        }

        // 逆向填充原始字节数组
        for (int j = bytes.length - 1, k = index - 1; j >= 0; j--, k--) {
            bytesTmp[k] = bytes[j];
        }

        return bytesTmp;
    }

    /**
     * hex字符串补位处理
     *
     * @param hexString hex字符串
     * @return hex字符串
     */
    public static String fullFillHexString(String hexString) {
        int len = hexString.length();
        int mold = len % 2;
        return mold > 0 ? "0" + hexString : hexString;
    }

    /**
     * 写short数据
     *
     * @param m             数据
     * @param out           ByteBuf
     * @param byteOrderEnum 字节序
     */
    public static void writeShort(int m, ByteBuf out, ByteOrderEnum byteOrderEnum) {
        //小字节序
        if (byteOrderEnum == ByteOrderEnum.LITTLE_ENDIAN) {
            out.writeShortLE(m);
        } else {
            //大字节序
            out.writeShort(m);
        }
    }

    /**
     * 写无符号数基础
     *
     * @param m    数据
     * @param buff ByteBuf数据缓存区
     */
    private static void writeUnSignByteBase(short m, ByteBuf buff) {
        assert m < 256 && m >= 0;
        buff.writeByte(m);
    }

}
