package cn.piesat.framework.dynamic.datasource.utils;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.Properties;

/**
 * <p/>
 * {@code @description}: 类字段转换为Properties工具类
 * <p/>
 * {@code @create}: 2025-02-11 10:49
 * {@code @author}: zhouxp
 */
public final class ClassField2PropertiesUtils {
    private ClassField2PropertiesUtils(){}
    public static <T> Properties toProperties( T t) {
        Properties properties = new Properties();
        Field[] declaredFields = t.getClass().getDeclaredFields();
        try {
            for (Field field : declaredFields) {
                field.setAccessible(true);
                Optional.ofNullable(field.get(t)).
                        ifPresent(v -> properties.setProperty(field.getName(), v.toString()));
            }
        } catch (Exception e) {
            throw new RuntimeException(t.getClass().getSimpleName() + "toProperties error, e=", e);
        }
        return properties;
    }
}
