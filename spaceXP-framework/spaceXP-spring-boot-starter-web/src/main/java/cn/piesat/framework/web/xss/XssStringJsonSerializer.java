package cn.piesat.framework.web.xss;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2024-04-14 13:48.
 *
 * @author zhouxp
 */
public class XssStringJsonSerializer extends JsonSerializer<String> {

    @Override
    public Class<String> handledType() {
        return String.class;
    }

    @Override
    public void serialize(String value, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        if (value != null) {
            jsonGenerator.writeString(HtmlUtils.htmlEscape(value));
        }
    }
}
