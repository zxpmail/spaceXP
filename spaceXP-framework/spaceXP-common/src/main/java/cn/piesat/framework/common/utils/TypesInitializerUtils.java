package cn.piesat.framework.common.utils;

import cn.piesat.framework.common.constants.CommonConstants;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * <p/>
 * {@code @description}: 类型初始化工具类
 * <p/>
 * {@code @create}: 2024-08-06 9:32
 * {@code @author}: zhouxp
 */
@SuppressWarnings("unchecked")
public class TypesInitializerUtils {
    /**
     * 初始化基本类型的默认值。
     *
     * @param <T> 基本类型或其包装类
     * @param typeClass 类型的Class对象
     * @return 默认值
     */
    public static <T> T initialize(Class<T> typeClass) {
        if (typeClass == boolean.class || typeClass == Boolean.class) {
            return (T) Boolean.FALSE;
        } else if (typeClass == byte.class || typeClass == Byte.class) {
            return (T) Byte.valueOf((byte) 0);
        } else if (typeClass == char.class || typeClass == Character.class) {
            return (T) Character.valueOf('\u0000');
        } else if (typeClass == short.class || typeClass == Short.class) {
            return (T) Short.valueOf((short) 0);
        } else if (typeClass == int.class || typeClass == Integer.class) {
            return (T) Integer.valueOf(0);
        } else if (typeClass == long.class || typeClass == Long.class) {
            return (T) Long.valueOf(0L);
        } else if (typeClass == float.class || typeClass == Float.class) {
            return (T) Float.valueOf(0.0f);
        } else if (typeClass == double.class || typeClass == Double.class) {
            return (T) Double.valueOf(0.0d);
        } else if (typeClass == String.class) {
            return (T) "";
        } else if (typeClass == Date.class) {
            return (T) Date.from(Instant.now());
        } else if (typeClass == BigDecimal.class) {
            return (T) new BigDecimal(0);
        } else if (typeClass == LocalTime.class) {
            return (T) LocalTime.MIDNIGHT;
        }else if (typeClass == LocalDateTime.class) {
            return (T) LocalTime.now();
        }else {
            throw new IllegalArgumentException(CommonConstants.Unsupported_type + typeClass);
        }
    }
}
