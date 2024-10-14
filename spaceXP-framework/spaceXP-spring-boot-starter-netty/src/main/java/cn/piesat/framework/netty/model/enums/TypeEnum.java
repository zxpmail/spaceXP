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
     * HEX字符串
     */
    HEX_STRING("hex_string"),
    /**
     * -2^31~2^31-1
     * -2,147,483,648~2,147,483,647
     */
    INT("int"),
    /**
     * 0~2^32
     * 0~4294967296L
     */
    UINT("uint"),
    /**
     * -2^15~2^15-1
     * -32768~32767
     */
    SHORT("short"),
    /**
     * 0~65535
     */
    USHORT("ushort"),
    /**
     * -2^7~2^7-1
     * -128~127
     */
    BYTE("byte"),

    /**
     * 0~256
     */
    UBYTE("ubyte");

    TypeEnum(String val) {
    }

    /**
     * 字符串匹配枚举类型
     *
     * @param value 字符串
     * @return 对应枚举
     */
    public static TypeEnum match(String value) {
        return valueOf(value.toUpperCase());
    }
}
