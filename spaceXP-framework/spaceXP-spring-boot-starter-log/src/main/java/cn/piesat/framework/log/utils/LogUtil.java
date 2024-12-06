package cn.piesat.framework.log.utils;


import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.UserAgentUtil;
import cn.piesat.framework.common.constants.CommonConstants;
import cn.piesat.framework.common.utils.ArgsUtils;
import cn.piesat.framework.log.annotation.OpLog;
import cn.piesat.framework.log.constants.LogConstants;
import cn.piesat.framework.common.model.enums.BusinessEnum;
import cn.piesat.framework.log.event.LogEvent;
import cn.piesat.framework.common.model.entity.OpLogEntity;
import cn.piesat.framework.log.properties.LogProperties;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiOperation;

import org.aspectj.lang.JoinPoint;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;

import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import static cn.piesat.framework.common.constants.CommonConstants.BROWSER;
import static cn.piesat.framework.common.constants.CommonConstants.IP;
import static cn.piesat.framework.common.constants.CommonConstants.OS;

/**
 * <p/>
 * {@code @description}  :日志工具类
 * <p/>
 * <b>@create:</b> 2022/11/28 9:11.
 *
 * @author zhouxp
 */
public class LogUtil {
    /***
     * 获取操作信息
     * @param point 切点
     * @return 操作描述
     */
    public static Map<String, Object> getControllerMethodOp(JoinPoint point, Integer logType) throws Exception {
        Map<String, Object> map = new HashMap<>();
        // 获取连接点目标类名
        String targetName = point.getTarget().getClass().getName();
        // 获取连接点签名的方法名
        String methodName = point.getSignature().getName();
        //获取连接点参数
        Object[] args = point.getArgs();
        //根据连接点类的名字获取指定类
        Class<?> targetClass = Class.forName(targetName);
        //获取类里面的方法
        Method[] methods = targetClass.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class<?>[] clazz = method.getParameterTypes();
                if (clazz.length == args.length) {
                    if (logType.equals(LogConstants.LOG_TYPE)) {
                        opLog(map, method);
                    } else {
                        swaggerLog(map, method);
                    }

                    break;
                }
            }
        }
        return map;
    }

    private static void opLog(Map<String, Object> map, Method method) {
        OpLog annotation = method.getAnnotation(OpLog.class);
        if (!ObjectUtils.isEmpty(annotation)) {
            map.put(LogConstants.DESCRIPTION, annotation.description());
            map.put(LogConstants.OP, annotation.op());
            map.put(LogConstants.SLOW_THRESHOLD_MILLS, annotation.slowThresholdMills());
        }
    }

    private static void swaggerLog(Map<String, Object> map, Method method) {
        ApiOperation annotation = method.getAnnotation(ApiOperation.class);
        if (!ObjectUtils.isEmpty(annotation)) {
            map.put(LogConstants.DESCRIPTION, annotation.value());
            map.put(LogConstants.OP, BusinessEnum.OTHER);
            map.put(LogConstants.SLOW_THRESHOLD_MILLS, -1);
        }
    }

    /**
     * 获取堆栈信息
     *
     * @param throwable 异常信息
     * @return 异常
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }

    public static void recordLog(ThreadLocal<OpLogEntity> logThreadLocal, JoinPoint joinPoint, LogProperties logProperties, String module) throws Exception {
        OpLogEntity opLogEntity = new OpLogEntity();
        opLogEntity.setConsumingTime(Instant.now().toEpochMilli());
        opLogEntity.setStartTime(LocalDateTime.now());


        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (ObjectUtils.isEmpty(requestAttributes)) {
            return;
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        if (ObjectUtils.isEmpty(request)) {
            return;
        }
        opLogEntity.setRequest(request);
        //将当前实体保存到threadLocal
        logThreadLocal.set(opLogEntity);

        if (!CollectionUtils.isEmpty(logProperties.getHeaders())) {
            Map<String, String> map = new HashMap<>();
            for (String s : logProperties.getHeaders()) {
                String header = request.getHeader(s);
                if (StringUtils.hasText(header)) {
                    map.put(s, header);
                }
            }
            opLogEntity.setHeaderSelfDefineValue(JSON.toJSONString(map));
        }
        String requestURI = request.getRequestURI();
        if (StringUtils.hasText(requestURI)) {
            String path = URLUtil.getPath(requestURI);
            if (StringUtils.hasText(path)) {
                opLogEntity.setActionUrl(path);
            }
        }

        opLogEntity.setRequestInfo(JSON.toJSONString(request.getParameterMap()));
        String os = request.getHeader(OS);
        String browser = request.getHeader(BROWSER);
        String ip = request.getHeader(IP);
        if (!StringUtils.hasText(ip)) {
            ip = ServletUtil.getClientIP(request);
        }
        opLogEntity.setIp(ip);
        opLogEntity.setLocation(ip);
        opLogEntity.setRequestMethod(request.getMethod());
        if (!StringUtils.hasText(os)) {
            String uaStr = request.getHeader("user-agent");
            if (StringUtils.hasText(uaStr)) {
                browser = UserAgentUtil.parse(uaStr).getBrowser().toString();
                os = UserAgentUtil.parse(uaStr).getOs().toString();
            }
        }
        opLogEntity.setBrowser(browser == null ? LogConstants.BROWSER : browser);
        opLogEntity.setOs(os == null ? LogConstants.OS : os);
        //访问目标方法的参数 可动态改变参数值
        Object[] args = joinPoint.getArgs();
        //获取执行的方法名
        opLogEntity.setActionMethod(joinPoint.getSignature().getName());
        // 类名
        opLogEntity.setClassPath(joinPoint.getTarget().getClass().getName());
        opLogEntity.setActionMethod(joinPoint.getSignature().getName());
        List<Object> argsList = ArgsUtils.processArgs(args);
        try {
            opLogEntity.setParams(JSON.toJSONString(argsList));
        } catch (Exception e) {
            e.printStackTrace();
            opLogEntity.setParams(JSON.toJSONString(LogConstants.INVALID_PARAMETER));
        }
        opLogEntity.setModule(module);
        Map<String, Object> op = LogUtil.getControllerMethodOp(joinPoint, logProperties.getLogFlag());
        BusinessEnum op1 = (BusinessEnum) op.get(LogConstants.OP);
        opLogEntity.setOp(op1.getOp());
        opLogEntity.setCode(op1.getCode());
        opLogEntity.setDescription(op.get(LogConstants.DESCRIPTION).toString());
    }

    public static void doAfterReturning(ThreadLocal<OpLogEntity> logThreadLocal, Object ret) {
        //得到当前线程的log对象
        OpLogEntity opLogEntity = logThreadLocal.get();
        opLogEntity.setFinishTime(LocalDateTime.now());
        long endTime = Instant.now().toEpochMilli();
        opLogEntity.setConsumingTime(endTime - opLogEntity.getConsumingTime());
        // 处理完请求，返回内容
        if (ret instanceof Exception) {
            opLogEntity.setType(LogConstants.error);
            opLogEntity.setExDetail(((Exception) ret).getMessage());
        } else {
            // 正常返回
            opLogEntity.setType(LogConstants.success);
            try {
                opLogEntity.setResponseData(JSON.toJSONString(ret));
            } catch (Exception e) {
                e.printStackTrace();
                opLogEntity.setResponseData(JSON.toJSONString(LogConstants.INVALID_RET));
            }

        }
    }

    public static void doAfterThrowable(ThreadLocal<OpLogEntity> logThreadLocal, Throwable e) {
        OpLogEntity opLogEntity = logThreadLocal.get();
        opLogEntity.setFinishTime(LocalDateTime.now());
        long endTime = Instant.now().toEpochMilli();
        opLogEntity.setConsumingTime(endTime - opLogEntity.getConsumingTime());
        // 异常
        opLogEntity.setType(LogConstants.error);
        // 异常对象
        opLogEntity.setExDetail(LogUtil.getStackTrace(e));
        // 异常信息
        opLogEntity.setExDesc(e.getMessage());
    }

    public static void doAfter(ThreadLocal<OpLogEntity> logThreadLocal, ApplicationContext applicationContext) throws Exception {
        OpLogEntity opLogEntity = logThreadLocal.get();
        HttpServletRequest request = opLogEntity.getRequest();
        String name = request.getHeader(CommonConstants.USERNAME);
        if (StringUtils.hasText(name)) {
            opLogEntity.setUserName(URLDecoder.decode(name, StandardCharsets.UTF_8.toString()));
        }
        String deptId = request.getHeader(CommonConstants.DEPT_ID);
        if (StringUtils.hasText(deptId)) {
            opLogEntity.setDeptId(deptId);
        }
        String deptName = request.getHeader(CommonConstants.DEPT_NAME);
        if (StringUtils.hasText(deptId)) {
            opLogEntity.setDeptName(URLDecoder.decode(deptName, StandardCharsets.UTF_8.toString()));
        }
        // 发布事件
        applicationContext.publishEvent(new LogEvent(opLogEntity));
        //移除当前log实体
        logThreadLocal.remove();
    }
}
