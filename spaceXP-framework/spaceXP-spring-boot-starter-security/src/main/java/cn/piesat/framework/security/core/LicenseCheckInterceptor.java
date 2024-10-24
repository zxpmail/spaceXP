package cn.piesat.framework.security.core;

import cn.piesat.framework.security.utils.LicenseUtil;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * {@code @description}: 配置拦截器
 * <p/>
 * {@code @create}: 2024-10-24 16:22
 * {@code @author}: zhouxp
 */
@Slf4j
public class LicenseCheckInterceptor implements HandlerInterceptor {
    private static final JsonMapper jsonMapper = new JsonMapper();
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //  校验证书是否有效
        boolean verifyResult = LicenseUtil.verify();
        if (verifyResult) {
            return true;
        } else {
            response.setCharacterEncoding("utf-8");
            Map<String, String> result = new HashMap<>(1);
            result.put("result", "您的证书无效，请核查服务器是否取得授权或重新申请证书！");
            response.getWriter().write(jsonMapper.writeValueAsString(result));
            return false;
        }
    }
}
