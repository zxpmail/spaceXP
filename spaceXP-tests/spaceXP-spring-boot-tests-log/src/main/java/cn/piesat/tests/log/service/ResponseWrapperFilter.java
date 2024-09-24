package cn.piesat.tests.log.service;

import cn.piesat.framework.log.annotation.OpLog;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p/>
 * {@code @description}:
 * <p/>
 * {@code @create}: 2024-09-24 9:26
 * {@code @author}: zhouxp
 */


@Slf4j
//@Component
public class ResponseWrapperFilter extends OncePerRequestFilter {
    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        Object handler = request.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);
        if (handler instanceof HandlerMethod) {
            OpLog oplog = ((HandlerMethod) handler).getMethod().getAnnotation(OpLog.class);
            if(oplog == null){
                filterChain.doFilter(request, response);
                return;
            }
        }
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        requestWrapper.getParameterNames();

        String requestBody = new String(requestWrapper.getContentAsByteArray(), requestWrapper.getCharacterEncoding());
        log.info("@ request User-Agent: " + request.getHeader("User-Agent"));
        log.info("@ request body: " + requestBody);


        filterChain.doFilter(requestWrapper, responseWrapper);
        String responseBody = new String(responseWrapper.getContentAsByteArray(), responseWrapper.getCharacterEncoding());
        log.info("@ response status: " + response.getStatus());
        log.info("@ response body: " + responseBody);
        log.info("end ......");
        // 可以在这里处理响应数据
        //byte[] body = responseWrapper.getContentAsByteArray();
        // 处理body，例如添加签名
        //responseWrapper.setHeader("X-Signature", "some-signature");

        // 必须调用此方法以将响应数据发送到客户端
        responseWrapper.copyBodyToResponse();
    }

}
