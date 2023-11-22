package cn.piesat.framework.log.core;

import cn.piesat.framework.common.model.entity.OpLogEntity;
import cn.piesat.framework.log.properties.LogProperties;
import cn.piesat.framework.log.utils.LogUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;


/**
 * <p/>
 * {@code @description}  :拦截含有OpLog注解AOP
 * <p/>
 * <b>@create:</b> 2023/10/8 14:09.
 *
 * @author zhouxp
 */
@Slf4j
@Aspect
public class SwaggerLogAspect {
    private final LogProperties logProperties;

    private final String module;
    private final ThreadLocal<OpLogEntity> logThreadLocal = new TransmittableThreadLocal<>();
    @Resource
    private ApplicationContext applicationContext;

    public SwaggerLogAspect(LogProperties logProperties, String module) {
        this.logProperties = logProperties;
        this.module = module;
    }

    /***
     * 定义controller切入点拦截规则，拦截OpLog注解的方法
     */
    @Pointcut("@annotation(io.swagger.annotations.ApiOperation)")
    public void logAspect() {

    }

    /***
     * 拦截控制层的操作日志
     */
    @Before(value = "logAspect()")
    public void recordLog(JoinPoint joinPoint) throws Throwable {
        LogUtil.recordLog(logThreadLocal, joinPoint, logProperties,module);
    }
    /**
     * 成功返回通知
     */
    @AfterReturning(returning = "ret", pointcut = "logAspect()")
    public void doAfterReturning(Object ret) {
        LogUtil.doAfterReturning(logThreadLocal,ret);
    }

    /**
     * 异常处理
     */
    @AfterThrowing(pointcut = "logAspect()", throwing = "e")
    public void doAfterThrowable(Throwable e) {
        LogUtil.doAfterThrowable(logThreadLocal,e);
    }

    /**
     *最终处理
     */
    @After("logAspect()")
    public void doAfter(JoinPoint point) throws Exception {
        LogUtil.doAfter(logThreadLocal,applicationContext);
    }
}
