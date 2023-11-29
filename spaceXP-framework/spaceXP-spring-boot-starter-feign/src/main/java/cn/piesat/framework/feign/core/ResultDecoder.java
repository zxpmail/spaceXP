package cn.piesat.framework.feign.core;


import cn.piesat.framework.common.annotation.NoApiResult;
import cn.piesat.framework.common.constants.CommonConstants;
import cn.piesat.framework.common.exception.BaseException;
import cn.piesat.framework.common.model.enums.CommonResponseEnum;
import cn.piesat.framework.common.model.vo.ApiResult;
import cn.piesat.framework.common.properties.CommonProperties;
import cn.piesat.framework.feign.annotation.HasApiResult;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;
import java.util.zip.GZIPInputStream;

/**
 * <p/>
 *
 * @author zhouxp
 * @description :结果解码
 * <p/>
 * <b>@create:</b> 2022/10/10 13:08.
 */
@RequiredArgsConstructor
@Slf4j
public class ResultDecoder implements Decoder {
    private final Decoder decoder;
    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object decode(Response response, Type type) throws IOException {
        if (!response.body().getClass().getName().contains(CommonConstants.FEIGN_NATIVE)) {
            Collection<String> values = response.headers().get(HttpEncoding.CONTENT_ENCODING_HEADER);
            if (Objects.nonNull(values) && !values.isEmpty() && values.contains(HttpEncoding.GZIP_ENCODING)) {
                byte[] compressed = Util.toByteArray(response.body().asInputStream());
                if ((compressed == null) || (compressed.length == 0)) {
                    return this.decoder.decode(response, type);
                }
                if (isCompressed(compressed)) {
                    final StringBuilder output = new StringBuilder();
                    final GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(compressed));
                    final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gis, StandardCharsets.UTF_8));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        output.append(line);
                    }
                    Response uncompressedResponse = response.toBuilder().body(output.toString().getBytes()).build();
                    return getObject(uncompressedResponse, type);
                }
            }
        }
        return getObject(response, type);
    }

    private Object getObject(Response response, Type type) throws IOException {

        Method method = response.request().requestTemplate().methodMetadata().method();
        HasApiResult annotation = method.getDeclaringClass().getAnnotation(HasApiResult.class);
        if (ObjectUtils.isEmpty(annotation)) {
            annotation = method.getAnnotation(HasApiResult.class);
        } else {
            if (!ObjectUtils.isEmpty(method.getAnnotation(NoApiResult.class))) {
                annotation = null;
            }
        }
        if (!ObjectUtils.isEmpty(annotation)) {
            ParameterizedTypeImpl resultType = new ParameterizedTypeImpl(ApiResult.class, new Type[]{type});
            Object decode = this.decoder.decode(response, resultType);
            if (decode instanceof ApiResult) {
                if (!((ApiResult<?>) decode).get(CommonProperties.Result.code).equals(CommonResponseEnum.SUCCESS.getCode())) {
                    throw new BaseException(CommonResponseEnum.ERROR);
                }
                if (Void.class == type || Void.TYPE == type) {
                    return null;
                }
                Object o = ((ApiResult<?>) decode).get(CommonProperties.Result.data);
                Class<?> aClass = (Class<?>) type;
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.convertValue(o, aClass);

            }
        }
        return this.decoder.decode(response, type);
    }

    private static boolean isCompressed(final byte[] compressed) {
        return (compressed[0] == (byte) (GZIPInputStream.GZIP_MAGIC)) && (compressed[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8));
    }

}
