package cn.piesat.tests.log.service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p/>
 * {@code @description}:
 * <p/>
 * {@code @create}: 2024-09-24 9:26
 * {@code @author}: zhouxp
 */



@Slf4j
@Component
public class ResponseWrapperFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        ContentCachingRequestWrapper  requestWrapper = new ContentCachingRequestWrapper(request);
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
