package cn.piesat.framework.feign.core;


import cn.piesat.framework.common.utils.ServletUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * <p/>
 *
 * @author zhouxp
 * @description :Feign设置header
 * <p/>
 * <b>@create:</b> 2022/10/10 14:12.
 */
@Slf4j
public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        HttpServletRequest request = ServletUtils.getRequest();
        if (request == null) {
            return;
        }
        Enumeration<String> headerNames = request.getHeaderNames();
        try {
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    String values = request.getHeader(name);
                    if (name.equals("content-length")) {
                        continue;
                    }
                    if (values.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE) && template.method().equals("POST")) {
                        continue;
                    }
                    template.header(name, values);
                }
            }
        }catch (Exception e){
            log.warn("可以忽略，异步时可能数据为空！",e);
        }

    }
}
