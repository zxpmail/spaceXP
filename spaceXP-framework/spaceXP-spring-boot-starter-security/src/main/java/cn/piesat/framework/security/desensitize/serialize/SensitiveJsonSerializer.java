package cn.piesat.framework.security.desensitize.serialize;



import cn.piesat.framework.security.desensitize.annotation.Desensitize;
import cn.piesat.framework.security.desensitize.enums.DesensitizeRuleEnums;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Objects;


/**
 * <p/>
 * {@code @description}  :数据脱敏JSON序列化工具
 * <p/>
 * <b>@create:</b> 2023/5/4 15:27.
 *
 * @author zhouxp
 */
@NoArgsConstructor
@AllArgsConstructor
public class SensitiveJsonSerializer extends JsonSerializer<String> implements ContextualSerializer {
    private DesensitizeRuleEnums rule;

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(rule.desensitize().apply(s));
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        Desensitize annotation = beanProperty.getAnnotation(Desensitize.class);
        if (Objects.nonNull(annotation) && Objects.equals(String.class, beanProperty.getType().getRawClass())) {
            this.rule = annotation.rule();
            return this;
        }
        return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
    }
}
