package cn.piesat.framework.log.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.URLUtil;
import cn.piesat.framework.common.utils.ArgsUtils;
import cn.piesat.framework.log.annotation.MdcLog;
import cn.piesat.framework.log.constants.MdcLogConstants;
import cn.piesat.framework.log.enums.MdcLogType;
import com.alibaba.fastjson.JSON;
import org.slf4j.MDC;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

import static cn.piesat.framework.common.constants.CommonConstants.APP_NAME;
import static cn.piesat.framework.common.constants.CommonConstants.FLOW_ID;
import static cn.piesat.framework.common.constants.CommonConstants.FLOW_NAME;
import static cn.piesat.framework.common.constants.CommonConstants.FLOW_TYPE;
import static cn.piesat.framework.common.constants.CommonConstants.IP;
import static cn.piesat.framework.common.constants.CommonConstants.REQ_PARAMETER;
import static cn.piesat.framework.common.constants.CommonConstants.REQ_URL;
import static cn.piesat.framework.common.constants.CommonConstants.USERNAME;
import static cn.piesat.framework.common.constants.CommonConstants.USER_ID;
import static cn.piesat.framework.log.constants.MdcLogConstants.LOG_TYPE;
import static cn.piesat.framework.log.constants.MdcLogConstants.TRACE_ID;

/**
 * <p/>
 * {@code @description}: mdc工具类
 * <p/>
 * {@code @create}: 2024-12-22 9:42
 * {@code @author}: zhouxp
 */
public class MdcUtils {
    private MdcUtils() {
    }

    public static void setFlowMdc(MdcLog mdcLog) {
        setMdcValue(LOG_TYPE, mdcLog.op().getOp());
        setMdcValue(FLOW_ID, mdcLog.flowId());
        setMdcValue(FLOW_TYPE, mdcLog.flowType());
        setMdcValue(FLOW_NAME, mdcLog.flowName());
        setMdcValue(APP_NAME, mdcLog.appName());
    }

    public static void setAppMdcValue(HttpServletRequest request, MdcLogType logType, Object[] args) {
        if (logType == MdcLogType.APP) {
            List<Object> argsList = ArgsUtils.processArgs(args);
            if (CollectionUtil.isEmpty(argsList)) {
                argsList.add("[]");
            }
            setMdcValue(REQ_PARAMETER, JSON.toJSONString(argsList));
            if (request == null) {
                return;
            }
            String requestURI = request.getRequestURI();
            if (StringUtils.hasText(requestURI)) {
                String path = URLUtil.getPath(requestURI);
                if (StringUtils.hasText(path)) {
                    setMdcValue(REQ_URL, path);
                }
            }
        }

    }

    public static void setRequestMdcValue(HttpServletRequest request) {
        setMdcValue(TRACE_ID, getHeaderValue(request, TRACE_ID, UUID.randomUUID().toString()));
        setMdcValue(USER_ID, getHeaderValue(request, "userId", ""));
        setMdcValue(USERNAME, getHeaderValue(request, "userName", ""));
        setMdcValue(IP, getHeaderValue(request, "ip", ""));
    }

    public static void setMdcValue(String key, String value) {
        MDC.put(key, value);
    }

    public static String getHeaderValue(HttpServletRequest request, String headerName, String defaultValue) {
        String value = request.getHeader(headerName);
        return StringUtils.hasText(value) ? value : defaultValue;
    }

    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (ObjectUtils.isEmpty(requestAttributes)) {
            return null;
        }
        if (!(requestAttributes instanceof ServletRequestAttributes)) {
            return null;
        }

        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        if (ObjectUtils.isEmpty(request)) {
            return null;
        }
        return request;
    }
}

