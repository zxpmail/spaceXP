package cn.piesat.tools.generator.utils;

import cn.piesat.tools.generator.constants.Constants;
import jdk.nashorn.internal.ir.ReturnNode;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;


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
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);
    public static  String LocalDateTime2String(LocalDateTime dateTime){
        return dateTime.format(formatter);
    }
    public static Optional<LocalDateTime> String2LocalDateTime(String dateTimeString) {
        // 输入验证
        if (dateTimeString == null || dateTimeString.trim().isEmpty()) {
            log.error("输入的日期时间字符串为null或空");
            return Optional.empty();
        }

        try {
            return Optional.of(LocalDateTime.parse(dateTimeString, formatter));
        } catch (DateTimeParseException e) {
            log.error("解析日期时间字符串失败: {}", dateTimeString, e);
            return Optional.empty();
        }
    }
}
