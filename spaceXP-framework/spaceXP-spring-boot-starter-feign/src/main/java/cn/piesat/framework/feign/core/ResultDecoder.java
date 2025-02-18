package cn.piesat.framework.feign.core;


import cn.piesat.framework.common.annotation.NoApiResult;
import cn.piesat.framework.common.constants.CommonConstants;
import cn.piesat.framework.common.model.vo.ApiMapResult;
import cn.piesat.framework.common.model.vo.ApiResult;
import cn.piesat.framework.common.properties.CommonProperties;
import cn.piesat.framework.feign.annotation.HasApiResult;


import com.alibaba.fastjson.JSON;
import feign.Response;
import feign.Util;
import feign.codec.Decoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.encoding.HttpEncoding;
import org.springframework.util.ObjectUtils;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.zip.GZIPInputStream;

/**
 * <p/>
 *
 * @author zhouxp
 * @description :对远程feign调用的结果进行解码功能
 * <p/>
 * <b>@create:</b> 2022/10/10 13:08.
 */
@RequiredArgsConstructor
@Slf4j
public class ResultDecoder implements Decoder {
    private final Decoder decoder;

    private static final  Integer OK_CODE=200;
    private final Boolean apiMapResultEnable;

    /**
     * feign 解码功能：当判断为gzip压缩时进行解压处理否则正常解码
     */
    @Override
    public Object decode(Response response, Type type) throws IOException {
        if (!response.body().getClass().getName().contains(CommonConstants.FEIGN_NATIVE)) {
            Collection<String> values = response.headers().get(HttpEncoding.CONTENT_ENCODING_HEADER);
            if (Objects.nonNull(values) && !values.isEmpty() && values.contains(HttpEncoding.GZIP_ENCODING)) {
                try (InputStream bodyStream = response.body().asInputStream()) {
                    byte[] compressed = Util.toByteArray(bodyStream);
                    if ((compressed == null) || (compressed.length == 0)) {
                        return this.decoder.decode(response, type);
                    }
                    if (isCompressed(compressed)) {
                        try (GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(compressed));
                             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gis, StandardCharsets.UTF_8))) {
                            final StringBuilder output = new StringBuilder();
                            String line;
                            while ((line = bufferedReader.readLine()) != null) {
                                output.append(line);
                            }
                            Response uncompressedResponse = response.toBuilder().body(output.toString().getBytes()).build();
                            return getObject(uncompressedResponse, type);
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to decompress content", e);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Failed to read body stream", e);
                }
            }
        }
        return getObject(response, type);
    }

    /**
     * 根据类上或者方法上含有HasApiResult注解进行包装返回值
     *
     * @param response response
     * @param type     Type
     * @return 解码
     * @throws IOException 解码异常
     */
    private Object getObject(Response response, Type type) throws IOException {

        Method method = response.request().requestTemplate().methodMetadata().method();

        HasApiResult annotation = null;
        if (method.getAnnotation(NoApiResult.class) != null) {
            log.info("Method or class has NoApiResult annotation");
        } else if ((annotation = method.getAnnotation(HasApiResult.class)) != null) {
            log.info("Method or class has HasApiResult annotation");
        } else if (method.getDeclaringClass().getAnnotation(NoApiResult.class) != null) {
            log.info("Method or class has NoApiResult annotation");
        } else if ((annotation = method.getDeclaringClass().getAnnotation(HasApiResult.class)) != null) {
            log.info("Method or class has HasApiResult annotation");
        }

        if (annotation != null) {
            ParameterizedTypeImpl resultType;
            try {
                if (apiMapResultEnable) {
                    resultType = new ParameterizedTypeImpl(ApiMapResult.class, new Type[]{type});
                } else {
                    resultType = new ParameterizedTypeImpl(ApiResult.class, new Type[]{type});
                }

                Object decode = this.decoder.decode(response, resultType);
                if (decode instanceof ApiMapResult) {
                    if (Void.class == type || Void.TYPE == type) {
                        log.error("Type is void !!! ");
                        return null;
                    }

                    Object o = ((ApiMapResult<?>) decode).get(CommonProperties.Result.data);
                    if (o == null) {
                        log.error("return data no data properties!!!");
                        return null;
                    }

                    String s = JSON.toJSONString(o);
                    return JSON.parseObject(s, type);
                } else if (decode instanceof ApiResult) {
                    ApiResult<?> apiResult = (ApiResult<?>) decode;
                    if(apiResult.getCode().equals(OK_CODE)){
                        return apiResult.getData();
                    }else{
                        log.error("code: {} message: {} ",apiResult.getCode(),apiResult.getMessage());
                        return null;
                    }
                }
            } catch (Exception e) {
                log.error("Error decoding response", e);
                throw new IOException("Error decoding response", e);
            }
        }
        // 如果没有注解，则直接解码
        return this.decoder.decode(response, type);
    }

    private static boolean isCompressed(final byte[] compressed) {
        return (compressed[0] == (byte) (GZIPInputStream.GZIP_MAGIC)) && (compressed[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8));
    }

}
