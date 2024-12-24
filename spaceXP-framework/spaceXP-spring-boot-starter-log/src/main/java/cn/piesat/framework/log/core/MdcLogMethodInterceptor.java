package cn.piesat.framework.log.core;

import cn.piesat.framework.common.utils.MethodInvocationUtil;
import cn.piesat.framework.log.annotation.MdcLog;
import cn.piesat.framework.log.constants.MdcLogConstants;
import cn.piesat.framework.log.enums.MdcLogType;
import cn.piesat.framework.log.utils.LogUtil;
import cn.piesat.framework.log.utils.MdcUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * <p/>
 * {@code @description}: mdc日志拦截器类
 * <p/>
 * {@code @create}: 2024-12-22 18:52
 * {@code @author}: zhouxp
 */
@Slf4j
public class MdcLogMethodInterceptor implements MethodInterceptor {
    private final String appInfo;
    private final String errorInfo;

    private final String logType;

    private final String logTypeCode;

    public MdcLogMethodInterceptor(String appInfo, String errorInfo, String logType, String logTypeCode) {
        this.appInfo = appInfo;
        this.errorInfo = errorInfo;
        this.logType = logType;
        this.logTypeCode = logTypeCode;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        MdcLog mdcLog;
        try {
            HttpServletRequest request = MdcUtils.getRequest();
            // 避免 AOP 时获取不到真正的实现
            Method method = MethodInvocationUtil.getActualMethod(invocation);
            mdcLog = method.getAnnotation(MdcLog.class);
            if (request != null) {
                MdcUtils.setRequestMdcValue(request);
            }
            if (mdcLog != null) {
                MdcUtils.setMdcValue(MdcLogConstants.LOG_TYPE, mdcLog.op().getOp());
                MdcUtils.setFlowMdc(mdcLog);
                MdcUtils.setAppMdcValue(request,mdcLog.op(),invocation.getArguments());
                MdcUtils.setMdcValue(MdcLogConstants.LOG_TYPE, mdcLog.op().getOp());
                MdcUtils.setMdcValue(MdcLogConstants.LOG_TYPE_CODE, mdcLog.op().getCode());
            }else{
                MdcUtils.setMdcValue(MdcLogConstants.LOG_TYPE, logType);
                MdcUtils.setAppMdcValue(request,MdcLogType.APP,invocation.getArguments());
                MdcUtils.setMdcValue(MdcLogConstants.LOG_TYPE_CODE, logTypeCode);
            }

            Object ret = invocation.proceed();
            if (mdcLog == null ) {
                MdcUtils.setMdcValue(MdcLogConstants.RESULT, JSON.toJSONString(ret));
                log.info(appInfo);
            }else if(mdcLog.op() == MdcLogType.APP){
                MdcUtils.setMdcValue(MdcLogConstants.RESULT, JSON.toJSONString(ret));
                log.info(appInfo);
            }
            return ret;
        } catch (Throwable e) {
            MdcUtils.setMdcValue(MdcLogConstants.LOG_TYPE, MdcLogType.FAIL.getOp());
            MdcUtils.setMdcValue(MdcLogConstants.CALL_STACK, LogUtil.getStackTrace(e));
            MdcUtils.setMdcValue(MdcLogConstants.LOG_TYPE_CODE, MdcLogType.FAIL.getCode());
            log.error(errorInfo, e);
            throw e;
        } finally {
            MDC.clear();
        }
    }
}


