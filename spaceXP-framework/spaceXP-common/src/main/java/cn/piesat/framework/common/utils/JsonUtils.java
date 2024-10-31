package cn.piesat.framework.common.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * <p/>
 * {@code @description}: JSON工具类
 * <p/>
 * {@code @create}: 2024-10-31 16:02
 * {@code @author}: zhouxp
 */
@Slf4j
public class JsonUtils {
    private static final JsonMapper JSON_MAPPER = JsonMapper.builder()
            .configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true)
            .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
            .configure(JsonParser.Feature.IGNORE_UNDEFINED, true)
            .build();

    private JsonUtils() {

    }

    public static String toJSONString(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        }
        try {
            return JSON_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("toJSONString failed", e);
        }
        return null;
    }

    public static String toJSONStringUnsafe(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        }
        try {
            return JSON_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("toJSONStringUnsafe failed", e);
        }
        return null;
    }

    public static byte[] toBytes(Object obj) {
        try {
            return JSON_MAPPER.writeValueAsBytes(obj);
        } catch (Exception e) {
            log.error("toBytes failed", e);
        }
        return null;
    }

    public static <T> T parseObject(String json, Class<T> clz) throws JsonProcessingException {
        return JSON_MAPPER.readValue(json, clz);
    }

    public static <T> T parseObject(byte[] b, Class<T> clz) throws IOException {
        return JSON_MAPPER.readValue(b, clz);
    }

    public static <T> T parseObject(byte[] b, TypeReference<T> typeReference) throws IOException {
        return JSON_MAPPER.readValue(b, typeReference);
    }

    public static <T> T parseObject(String json, TypeReference<T> typeReference) throws IOException {
        return JSON_MAPPER.readValue(json, typeReference);
    }

    public static <T> T parseObjectIgnoreException(String json, Class<T> clz) {
        if (!StringUtils.hasText(json)) {
            return null;
        }
        try {
            return JSON_MAPPER.readValue(json, clz);
        } catch (Exception e) {
            log.error("unable to parse json string to object,current string:{}", json, e);

        }
        return null;
    }

    public static <T> T parseObjectUnsafe(String json, Class<T> clz) {
        try {
            return JSON_MAPPER.readValue(json, clz);
        } catch (Exception e) {
            log.error("unable to parse json string to object,current string:{}", json, e);
        }
        return null;
    }
}
