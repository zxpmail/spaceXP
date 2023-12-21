package cn.piesat.framework.web.core;

import cn.piesat.framework.web.properties.WebProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * {@code @description}  : 在bean初始化时，把返回值处理类注册到springMVC中
 * <p/>
 * <b>@create:</b> 2023/9/26 16:06.
 *
 * @author zhouxp
 */
@RequiredArgsConstructor
public class UniformApiResultWrapper implements InitializingBean {

    private final Boolean apiMapResultEnable;
    private final WebProperties webProperties;

    @Resource
    private  RequestMappingHandlerAdapter requestMappingHandlerAdapter;


    @Override
    public void afterPropertiesSet()  {
        List<HandlerMethodReturnValueHandler> unmodifiableList = this.requestMappingHandlerAdapter.getReturnValueHandlers();
        assert unmodifiableList != null;
        List<HandlerMethodReturnValueHandler> list = new ArrayList<>(unmodifiableList.size());
        for (HandlerMethodReturnValueHandler returnValueHandler : unmodifiableList) {
            if (returnValueHandler instanceof RequestResponseBodyMethodProcessor) {
                list.add(new ApiResponseHandlerMethodReturnValueHandler(returnValueHandler,apiMapResultEnable,webProperties.getIgnoreUrls()));
            } else {
                list.add(returnValueHandler);
            }
        }
        this.requestMappingHandlerAdapter.setReturnValueHandlers(list);
    }
}
