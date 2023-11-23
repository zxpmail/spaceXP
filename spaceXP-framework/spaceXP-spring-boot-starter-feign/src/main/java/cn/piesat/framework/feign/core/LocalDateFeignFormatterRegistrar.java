package cn.piesat.framework.feign.core;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.io.IOException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2023/10/25 15:44.
 *
 * @author zhouxp
 */
public class LocalDateFeignFormatterRegistrar extends LocalDateTimeDeserializer {

    // 省略不需要修改的代码
    private final DateTimeFormatter dateTimeFormatter;

    public LocalDateFeignFormatterRegistrar(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    /**
     * 关键方法
     */
    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        if (parser.hasTokenId(6)) {
            // 修改了这个分支里面的代码
            String string = parser.getText().trim();
            if (string.length() == 0) {
                return !this.isLenient() ?  this._failForNotLenient(parser, context, JsonToken.VALUE_STRING) : null;
            } else {
                return convert(string);
            }
        } else {
            return this.deserialize(parser,context);
            // 省略了没有修改的代码
        }
    }

    public LocalDateTime convert(String source) {
        source = source.trim();
        if ("".equals(source)) {
            return null;
        }
        if (source.matches("^\\d{4}-\\d{1,2}$")) {
            // yyyy-MM
            return LocalDateTime.parse(source + "-01 00:00:00", dateTimeFormatter);
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            // yyyy-MM-dd
            return LocalDateTime.parse(source + " 00:00:00", dateTimeFormatter);
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
            // yyyy-MM-dd HH:mm
            return LocalDateTime.parse(source + ":00", dateTimeFormatter);
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            // yyyy-MM-dd HH:mm:ss
            return LocalDateTime.parse(source, dateTimeFormatter);
        } else {
            throw new IllegalArgumentException("Invalid datetime value '" + source + "'");
        }
    }
}
