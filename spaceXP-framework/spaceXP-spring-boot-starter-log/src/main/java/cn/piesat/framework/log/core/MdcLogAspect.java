package cn.piesat.framework.log.core;

import cn.piesat.framework.log.annotation.MdcLog;
import cn.piesat.framework.log.constants.MdcLogConstants;
import cn.piesat.framework.log.enums.MdcLogType;
import cn.piesat.framework.log.utils.LogUtil;
import cn.piesat.framework.log.utils.MdcUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * <p/>
 * {@code @description}: mdc系统日志切面
 * <p/>
 * {@code @create}: 2024-12-22 18:52
 * {@code @author}: zhouxp
 */
@Slf4j
@Aspect
public class MdcLogAspect {
    private final String appInfo;
    private final String errorInfo;

    private final ThreadLocal<MdcLogType> logThreadLocal = new ThreadLocal<>();

    public MdcLogAspect(String appInfo, String errorInfo) {
        this.appInfo = appInfo;
        this.errorInfo = errorInfo;
    }


    /***
     * 定义controller切入点拦截规则，拦截OpLog注解的方法
     */
    @Pointcut("@annotation(cn.piesat.framework.log.annotation.MdcLog)")
    public void logAspect() {
    }

    /***
     * 拦截控制层的操作日志
     */
    @Before(value = "logAspect()")
    public void recordLog(JoinPoint joinPoint)  {
        try {
            HttpServletRequest request = MdcUtils.getRequest();
            if(request==null){
                return;
            }
            Method method=((MethodSignature)joinPoint.getSignature()).getMethod();
            MdcLog mdcLog = method.getAnnotation(MdcLog.class);
            logThreadLocal.set(mdcLog.op());
            MdcUtils.setRequestMdcValue(request);
            MdcUtils.setFlowMdc(mdcLog);
            MdcUtils.setAppMdcValue(request,MdcLogType.APP,joinPoint.getArgs());
        } catch (Exception e) {
            // 记录异常日志
            log.error("Error setting MDC context: " + e.getMessage());
        }
    }
    /**
     * 返回通知
     *
     * @param ret 返回值
     */
    @AfterReturning(returning = "ret", pointcut = "logAspect()")
    public void doAfterReturning(Object ret) {
        if (ret instanceof Exception) {
            MdcUtils.setMdcValue(MdcLogConstants.CALL_STACK, LogUtil.getStackTrace((Throwable) ret));
        } else {
            MdcLogType logType = logThreadLocal.get();
            if(logType==MdcLogType.APP){
                MdcUtils.setMdcValue(MdcLogConstants.RESULT, JSON.toJSONString(ret));
                log.info(appInfo);
            }
        }
    }

    /**
     * 异常通知
     *
     * @param e 异常
     */
    @AfterThrowing(pointcut = "logAspect()", throwing = "e")
    public void doAfterThrowable(Throwable e) {
        MdcUtils.setMdcValue(MdcLogConstants.LOG_TYPE,MdcLogType.FAIL.getOp());
        MdcUtils.setMdcValue(MdcLogConstants.CALL_STACK, LogUtil.getStackTrace(e));
        log.error(errorInfo, e);
    }

    @After("logAspect()")
    public void doAfter() {
        MDC.clear();
        logThreadLocal.remove();
    }
}


