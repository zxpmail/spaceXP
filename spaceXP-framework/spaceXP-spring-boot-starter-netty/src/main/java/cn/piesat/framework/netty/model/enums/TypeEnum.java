package cn.piesat.framework.netty.model.enums;

/**
 * <p/>
 * {@code @description}: netty类型操作枚举类
 * <p/>
 * {@code @create}: 2024-10-12 13:06
 * {@code @author}: zhouxp
 */
public enum TypeEnum {
    /**
     * 字符串
     */
    TYPE_STRING("string"),

    /**
     * Binary-Coded Decimal
     * bcd码 8421码
     * 4位二进制数表示1位十进制数
     */
    TYPE_BCD8421("bcd8421"),
    /**
     * 时间字符串
     */
    TYPE_DATE_STRING("date_string"),
    /**
     * 枚举byte
     */
    TYPE_ENUM_BYTE("enum|byte"),

    /**
     * 枚举int
     */
    TYPE_ENUM_INT("enum|int"),

    /**
     * 枚举字符串
     */
    TYPE_ENUM_STRING("enum|string"),

    /**
     * 枚举HEX字符串
     */
    TYPE_ENUM_HEX_STRING("enum|hex_string"),

    /**
     * HEX字符串
     */
    TYPE_HEX_STRING("hex_string"),

    /**
     * -2^31~2^31-1
     * -2,147,483,648~2,147,483,647
     */
    TYPE_INT("int"),
    /**
     * 0~2^32
     * 0~4294967296L
     */
    TYPE_UINT("uint"),
    /**
     * -2^15~2^15-1
     * -32768~32767
     */
    TYPE_SHORT("short"),
    /**
     * 0~65535
     */
    TYPE_USHORT("ushort"),
    /**
     * -2^7~2^7-1
     * -128~127
     */
    TYPE_BYTE("byte"),

    /**
     * 0~256
     */
    TYPE_UBYTE("ubyte"),

    /**
     * 多位同选
     */
    TYPE_MULTI_BIT("multi_bit"),
    /**
     * 位
     */
    TYPE_BIT("bit");

    TypeEnum(String val) {
    }


    /**
     * 字符串匹配枚举类型
     *
     * @param value 字符串
     * @return 对应枚举
     */
    public static TypeEnum match(String value) {
        String str = "TYPE_";
        if (value.indexOf("|") > 0) {
            value = value.replace("|", "_");
        }
        str += value.toUpperCase();
        return valueOf(str);
    }
}
