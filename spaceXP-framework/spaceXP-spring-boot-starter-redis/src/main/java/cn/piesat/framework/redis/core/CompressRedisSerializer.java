package cn.piesat.framework.redis.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.xerial.snappy.Snappy;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * <p/>
 * {@code @description}  :压缩
 * <p/>
 * <b>@create:</b> 2023/12/1 9:34.
 *
 * @author zhouxp
 */
public class CompressRedisSerializer<T> implements RedisSerializer<T> {

    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private Class<T> clazz;

    public CompressRedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t){
        if (t == null) {
            return new byte[0];
        }
        try {
            return Snappy.compress(JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    public T deserialize(byte[] bytes)  {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        String str = null;
        try {
            str = new String(Snappy.uncompress(bytes), DEFAULT_CHARSET);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JSON.parseObject(str, clazz);
    }
}
