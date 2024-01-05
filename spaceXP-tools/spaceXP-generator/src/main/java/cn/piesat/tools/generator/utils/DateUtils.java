package cn.piesat.tools.generator.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * <p/>
 * {@code @description}  : 日期处理
 * <p/>
 * <b>@create:</b> 2024/1/4 15:55.
 *
 * @author zhouxp
 */
@Slf4j
public class DateUtils {
    /** 时间格式(yyyy-MM-dd) */
    public final static String DATE_PATTERN = "yyyy-MM-dd";
    /** 时间格式(yyyy-MM-dd HH:mm:ss) */
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    // 使用静态代码块初始化SimpleDateFormat对象
    private static final ThreadLocal<SimpleDateFormat> THREAD_LOCAL_DATE_FORMATTER = ThreadLocal.withInitial(() -> {
        try {
            return new SimpleDateFormat(DATE_PATTERN);
        } catch (IllegalArgumentException e) {
            // 如果给定模式非法，抛出异常或返回错误对象
            throw new IllegalStateException("Date pattern is invalid: " + DATE_PATTERN, e);
        }
    });

    public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    public static String format(Date date, String pattern) {
        if (date != null) {
            return format(pattern, date);
        }
        return null;
    }

    private static String format(String pattern, Date date) {
        if (date == null) {
            return null;
        }
        return THREAD_LOCAL_DATE_FORMATTER.get().format(date);
    }

    public static Date parse(String date, String pattern) {
        if (date == null || date.isEmpty()) {
            throw new IllegalArgumentException("Date string cannot be null or empty");
        }
        if (pattern == null || pattern.isEmpty()) {
            pattern = DATE_PATTERN; // 使用默认模式，避免每次调用时都解析相同的模式
        }

        try {
            return THREAD_LOCAL_DATE_FORMATTER.get().parse(date);
        } catch (ParseException e) {
            // 更详细的日志记录
            log.error("Failed to parse date: " + date + ", pattern: " + pattern, e);
            // 抛出异常或者返回自定义的错误对象
            throw new RuntimeException("Date parsing failed", e);
        }
    }

    // 通常，这个类会被声明为静态的，以便可以直接访问其中的方法，而不需要创建类的实例。
    private DateUtils() {
        // 私有构造函数，以防止实例化
    }
}
