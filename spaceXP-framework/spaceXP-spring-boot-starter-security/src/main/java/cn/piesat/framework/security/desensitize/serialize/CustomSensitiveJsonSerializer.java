package cn.piesat.framework.security.desensitize.serialize;


import cn.hutool.core.text.CharSequenceUtil;
import cn.piesat.framework.security.desensitize.annotation.CustomDesensitize;
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
public class CustomSensitiveJsonSerializer extends JsonSerializer<String> implements ContextualSerializer {
    /**
     * 开始位置（包含）
     */
    private Integer start;

    /**
     * 结束位置（不包含）
     */
    private Integer end;


    private String symbol;


    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(String.valueOf(CharSequenceUtil.replace(s,start,end,symbol)));
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty==null){
            return serializerProvider.findNullValueSerializer(null);
        }
        // 判断数据类型是否为String类型
        if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
            // 获取定义的注解
            CustomDesensitize customDesensitize = beanProperty.getAnnotation(CustomDesensitize.class);
            // 为null
            if (customDesensitize == null) {
                customDesensitize = beanProperty.getContextAnnotation(CustomDesensitize.class);
            }
            // 不为null
            if (customDesensitize != null) {
                // 创建定义的序列化类的实例并且返回，入参为注解定义的type,开始位置，结束位置。
                return new CustomSensitiveJsonSerializer( customDesensitize.start(),
                        customDesensitize.end(),customDesensitize.symbol());
            }
        }

        return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
    }
}
