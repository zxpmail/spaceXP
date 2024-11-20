package cn.piesat.tools.gateway.filter;

import cn.piesat.tools.gateway.model.GatewayLog;

import cn.piesat.tools.gateway.properties.GatewayProperties;

import cn.piesat.tools.gateway.utils.GatewayUtil;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * <p/>
 * {@code @description}: 日志过滤器
 * <p/>
 * {@code @create}: 2024-11-19 16:43
 * {@code @author}: zhouxp
 */
@Slf4j
@SuppressWarnings({"rawtypes","unchecked","unused"})
public class AccessLogFilter implements GlobalFilter, Ordered {
    private final GatewayProperties gatewayProperties;
    private final List<HttpMessageReader<?>> messageReaders = HandlerStrategies.withDefaults().messageReaders();
    /**
     * default HttpMessageReader.
     */
    private static final List<HttpMessageReader<?>> MESSAGE_READERS = HandlerStrategies.withDefaults().messageReaders();

    public AccessLogFilter(GatewayProperties gatewayProperties) {
        this.gatewayProperties = gatewayProperties;
    }

    @Override
    public int getOrder() {
        return -101;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!gatewayProperties.getIsAuthentication() || !gatewayProperties.getEnabled() || GatewayUtil.isIgnoredPatterns(exchange, gatewayProperties.getIgnorePaths())) {
            return chain.filter(exchange);
        }
        GatewayLog gatewayLog = fillGatewayLogFromExchange(exchange);
        ServerHttpRequest request = exchange.getRequest();
        MediaType mediaType = request.getHeaders().getContentType();
        if (Objects.isNull(mediaType)) {
            return execNormalLog(exchange, chain, gatewayLog);
        }
        gatewayLog.setRequestContentType(mediaType.getType() + "/" + mediaType.getSubtype());
        // 对不同的请求类型做相应的处理
        if (MediaType.APPLICATION_JSON.isCompatibleWith(mediaType)) {
            return execBodyLog(exchange, chain, gatewayLog);
        } else if (MediaType.MULTIPART_FORM_DATA.isCompatibleWith(mediaType) || MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType)) {
            return execFormData(exchange, chain, gatewayLog);
        } else {
            return execBasicLog(exchange, chain, gatewayLog);
        }
    }

    private Mono<Void> execNormalLog(ServerWebExchange exchange, GatewayFilterChain chain, GatewayLog gatewayLog) {
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            ServerHttpResponse response = exchange.getResponse();
            int value = getStatusCodeValue(response);
            gatewayLog.setCode(value);
            long executeTime = getExecutionTime(gatewayLog.getRequestTime());
            gatewayLog.setExecuteTime(executeTime);
            Map<String, String> paramsMap = getQueryParamsMap(exchange.getRequest());
            gatewayLog.setQueryParams(paramsMap);
            GatewayUtil.printLog(gatewayLog);
        }));
    }
    /**
     * 解决 request body 只能读取一次问题，
     * 参考: org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory
     */
    private Mono<Void> execBodyLog(ServerWebExchange exchange, GatewayFilterChain chain, GatewayLog gatewayLog) {
        ServerRequest serverRequest = ServerRequest.create(exchange, messageReaders);
        Mono<String> modifiedBody = serverRequest.bodyToMono(String.class).flatMap(body -> {
            gatewayLog.setRequestBody(body);
            return Mono.just(body);
        });
        // 通过 BodyInserter 插入 body(支持修改body), 避免 request body 只能获取一次
        BodyInserter<Mono<String>, ReactiveHttpOutputMessage> bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());
        // the new content type will be computed by bodyInserter
        // and then set in the request decorator
        headers.remove(HttpHeaders.CONTENT_LENGTH);
        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
        return bodyInserter.insert(outputMessage, new BodyInserterContext()).then(Mono.defer(() -> {                    // 重新封装请求
            ServerHttpRequest decoratedRequest = requestDecorate(exchange, headers, outputMessage);                    // 记录响应日志
            ServerHttpResponseDecorator decoratedResponse = recordResponseLog(exchange, gatewayLog);                    // 记录普通的
            return chain.filter(exchange.mutate().request(decoratedRequest).response(decoratedResponse).build()).then(Mono.fromRunnable(() -> {                                // 打印日志
                GatewayUtil.printLog(gatewayLog);
            }));
        }));
    }
    private Map<String, String> getQueryParamsMap(ServerHttpRequest request) {
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        Map<String, String> paramsMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(queryParams)) {
            for (Map.Entry<String, List<String>> entry : queryParams.entrySet()) {
                paramsMap.put(entry.getKey(), joinWithComma(entry.getValue()));
            }
        }
        return paramsMap;
    }

    /**
     * 获得当前请求分发的路由
     */
    private Route getGatewayRoute(ServerWebExchange exchange) {
        return Objects.requireNonNull(exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR));
    }

    private GatewayLog fillGatewayLogFromExchange(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String requestPath = request.getPath().pathWithinApplication().value();
        Route route = getGatewayRoute(exchange);
        String ip = GatewayUtil.analysisSourceIp(request);
        GatewayLog gatewayLog = new GatewayLog();
        gatewayLog.setSchema(request.getURI().getScheme());
        gatewayLog.setMethod(request.getMethodValue());
        gatewayLog.setRequestPath(requestPath);
        gatewayLog.setTargetServer(route.getId());
        gatewayLog.setIp(ip);
        gatewayLog.setRequestTime(LocalDateTime.now());
        return gatewayLog;
    }

    public static String joinWithComma(List<String> list) {
        return list.stream()
                .filter(Objects::nonNull)
                .reduce((a, b) -> a + "," + b)
                .orElse("");
    }

    public static long getExecutionTime(LocalDateTime startDateTime) {
        return ChronoUnit.MILLIS.between(startDateTime, LocalDateTime.now());
    }

    private int getStatusCodeValue(ServerHttpResponse response) {
        return response.getStatusCode() != null ? response.getStatusCode().value() : -1;
    }
    /**
     * 读取form-data数据
     */

    private Mono<Void> execFormData(ServerWebExchange exchange, GatewayFilterChain chain, GatewayLog accessLog) {
        return DataBufferUtils.join(exchange.getRequest().getBody()).flatMap(dataBuffer -> {
            final ServerHttpRequest mutatedRequest = getServerHttpRequest(exchange, dataBuffer);
            final HttpHeaders headers = exchange.getRequest().getHeaders();
            if (headers.getContentLength() == 0) {
                return chain.filter(exchange);
            }
            ResolvableType resolvableType;
            if (MediaType.MULTIPART_FORM_DATA.isCompatibleWith(headers.getContentType())) {
                resolvableType = ResolvableType.forClassWithGenerics(MultiValueMap.class, String.class, Part.class);
            } else {
                //解析 application/x-www-form-urlencoded
                resolvableType = ResolvableType.forClass(String.class);
            }

            return MESSAGE_READERS.stream().filter(reader -> reader.canRead(resolvableType, mutatedRequest.getHeaders().getContentType())).findFirst().orElseThrow(() -> new IllegalStateException("no suitable HttpMessageReader.")).readMono(resolvableType, mutatedRequest, Collections.emptyMap()).flatMap(resolvedBody -> {
                if (resolvedBody instanceof MultiValueMap) {
                    LinkedMultiValueMap map = (LinkedMultiValueMap) resolvedBody;
                    if (!CollectionUtils.isEmpty(map)) {
                        StringBuilder builder = new StringBuilder();
                        final Part bodyPartInfo = (Part) ((MultiValueMap) resolvedBody).getFirst("body");
                        if (bodyPartInfo instanceof FormFieldPart) {
                            String body = ((FormFieldPart) bodyPartInfo).value();
                            builder.append("body=").append(body);
                        }
                        accessLog.setRequestBody(builder.toString());
                    }
                } else {
                    accessLog.setRequestBody((String) resolvedBody);
                }

                //获取响应体
                ServerHttpResponseDecorator decoratedResponse = recordResponseLog(exchange, accessLog);
                return chain.filter(exchange.mutate().request(mutatedRequest).response(decoratedResponse).build()).then(Mono.fromRunnable(() -> {                                    // 打印日志
                    // 打印响应的日志
                    GatewayUtil.printLog(accessLog);
                }));
            });
        });
    }


    private Mono<Void> execBasicLog(ServerWebExchange exchange, GatewayFilterChain chain, GatewayLog accessLog) {
        return DataBufferUtils.join(exchange.getRequest().getBody()).flatMap(dataBuffer -> {
            final ServerHttpRequest mutatedRequest = getServerHttpRequest(exchange, dataBuffer);
            StringBuilder builder = new StringBuilder();
            MultiValueMap<String, String> queryParams = exchange.getRequest().getQueryParams();
            if (!CollectionUtils.isEmpty(queryParams)) {
                for (Map.Entry<String, List<String>> entry : queryParams.entrySet()) {
                    builder.append(entry.getKey()).append("=").append(entry.getValue()).append(",");
                }
            }
            accessLog.setRequestBody(builder.toString());            //获取响应体
            ServerHttpResponseDecorator decoratedResponse = recordResponseLog(exchange, accessLog);
            return chain.filter(exchange.mutate().request(mutatedRequest).response(decoratedResponse).build()).then(Mono.fromRunnable(() -> {                        // 打印日志
                GatewayUtil.printLog(accessLog);
            }));
        });
    }

    private static ServerHttpRequest getServerHttpRequest(ServerWebExchange exchange, DataBuffer dataBuffer) {
        DataBufferUtils.retain(dataBuffer);
        final Flux<DataBuffer> cachedFlux = Flux.defer(() -> Flux.just(dataBuffer.slice(0, dataBuffer.readableByteCount())));
        return new ServerHttpRequestDecorator(exchange.getRequest()) {
            @Override
            public Flux<DataBuffer> getBody() {
                return cachedFlux;
            }
            @Override
            public MultiValueMap<String, String> getQueryParams() {
                return UriComponentsBuilder.fromUri(exchange.getRequest().getURI()).build().getQueryParams();
            }
        };
    }

    /**
     * 请求装饰器，重新计算 headers
     */
    private ServerHttpRequestDecorator requestDecorate(ServerWebExchange exchange, HttpHeaders headers, CachedBodyOutputMessage outputMessage) {
        return new ServerHttpRequestDecorator(exchange.getRequest()) {
            @Override
            public HttpHeaders getHeaders() {
                long contentLength = headers.getContentLength();
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.putAll(super.getHeaders());
                if (contentLength > 0) {
                    httpHeaders.setContentLength(contentLength);
                } else {
                    httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                }
                return httpHeaders;
            }

            @Override
            public Flux<DataBuffer> getBody() {
                return outputMessage.getBody();
            }
        };
    }

    /**
     * 记录响应日志
     * 通过 DataBufferFactory 解决响应体分段传输问题。
     */
    private ServerHttpResponseDecorator recordResponseLog(ServerWebExchange exchange, GatewayLog gatewayLog) {
        ServerHttpResponse response = exchange.getResponse();
        DataBufferFactory bufferFactory = response.bufferFactory();
        return new ServerHttpResponseDecorator(response) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    // 计算执行时间
                    long executeTime = getExecutionTime(gatewayLog.getRequestTime());
                    ;
                    gatewayLog.setExecuteTime(executeTime);
                    // 获取响应类型，如果是 json 就打印
                    String originalResponseContentType = exchange.getAttribute(ServerWebExchangeUtils.ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR);//
                    gatewayLog.setCode(Objects.requireNonNull(this.getStatusCode()).value());
                    //
                    if (Objects.equals(this.getStatusCode(), HttpStatus.OK)
                            && StringUtils.hasText(originalResponseContentType)
                            && originalResponseContentType.contains("application/json")) {
                        Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                        return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                            // 合并多个流集合，解决返回体分段传输
                            DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                            DataBuffer join = dataBufferFactory.join(dataBuffers);
                            byte[] content = new byte[join.readableByteCount()];
                            // 释放掉内存
                            join.read(content);
                            DataBufferUtils.release(join);
                            return bufferFactory.wrap(content);
                        }));
                    }
                }
                return super.writeWith(body);
            }
        };
    }
}


