package cn.piesat.framework.permission.url.core;

import cn.piesat.framework.common.constants.CommonConstants;
import cn.piesat.framework.common.model.vo.ApiResult;
import cn.piesat.framework.permission.url.enums.UrlPermissionResponseEnum;
import cn.piesat.framework.permission.url.properties.UrlPermissionProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * <p/>
 * {@code @description}  :自定义URL过滤器
 * 过滤器内容主要包含
 * ①：不拦截的路径直接放过；
 * ②：校验token；
 * ③：登出接口处理，删除token；
 * ④：用户权限校验，判断是否有访问权限；
 * ⑤：token续期和向heard头添加userId和userName。
 * <p/>
 * <b>@create:</b> 2023/9/12 9:48.
 *
 * @author zhouxp
 */
@Slf4j
public class UrlPermissionFilter implements Filter {

    @Resource
    private UserUrlPermissionService userUrlPermissionService;
    private final UrlPermissionProperties urlPermissionProperties;
    private static final PathMatcher antPathMatcher = new AntPathMatcher();
    private final static ObjectMapper objectMapper = new ObjectMapper();

    public UrlPermissionFilter(UrlPermissionProperties urlPermissionProperties) {
        this.urlPermissionProperties = urlPermissionProperties;
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        userUrlPermissionService.checkToken();
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uri = request.getRequestURI();
        //判断忽略url
        List<String> whiteList = urlPermissionProperties.getWhiteList();
        if (!CollectionUtils.isEmpty(whiteList)) {
            for (String path : whiteList) {
                if (antPathMatcher.match(path, uri)) {
                    log.debug("不做拦截的URL: {}", uri);
                    filterChain.doFilter(request, response);
                    return;
                }
            }
        }
        String currentUser = request.getHeader(CommonConstants.USERNAME);
        String id = request.getHeader(CommonConstants.USER_ID);
        Map<String, String> headerValue = userUrlPermissionService.getHeaderValue();
        if (!CollectionUtils.isEmpty(headerValue)) {
            HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(request);
            headerValue.forEach(requestWrapper::addHeader);
            if (!StringUtils.hasText(currentUser)) {
                currentUser = headerValue.get(CommonConstants.USERNAME);
            }
            if (!StringUtils.hasText(id)) {
                currentUser = headerValue.get(CommonConstants.USER_ID);
            }
        }
        if ((!StringUtils.hasText(id))) {
            wrapperResponse(response, ApiResult.fail(UrlPermissionResponseEnum.TOKEN_NOT_VALID));
            return;
        }
        //判断忽略用户
        List<String> users = urlPermissionProperties.getUserList();

        if (!CollectionUtils.isEmpty(users) && StringUtils.hasText(currentUser)) {
            if (users.contains(currentUser)) {
                filterChain.doFilter(request, response);
                return;
            }
        }


        List<String> url = userUrlPermissionService.getUrl(Long.parseLong(id));

        boolean pathCanVisit = false;
        if (!CollectionUtils.isEmpty(url)) {
            for (String path : url) {
                if (antPathMatcher.match(path, uri)) {
                    pathCanVisit = true;
                    break;
                }
            }
        }
        if (!pathCanVisit) {
            log.info("没有权限！");
            wrapperResponse(response, ApiResult.fail(UrlPermissionResponseEnum.NO_PERMISSION));

            return;
        }
        //续租
        userUrlPermissionService.tokenRenewed();


        filterChain.doFilter(request, response);

    }

    private static void wrapperResponse(HttpServletResponse response, ApiResult apiResult) throws IOException {
        response.setContentLength(-1);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter jsonOut = response.getWriter();
        jsonOut.write(objectMapper.writeValueAsString(apiResult));
        jsonOut.flush();
        jsonOut.close();
    }
}
