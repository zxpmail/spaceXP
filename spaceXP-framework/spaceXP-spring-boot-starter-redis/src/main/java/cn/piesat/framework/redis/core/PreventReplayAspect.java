package cn.piesat.framework.redis.core;

import cn.piesat.framework.common.exception.BaseException;
import cn.piesat.framework.redis.annotation.PreventReplay;
import com.alibaba.fastjson2.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * <p/>
 * {@code @description}  :防刷切面实现类
 * <p/>
 * <b>@create:</b> 2023/12/19 15:05.
 *
 * @author zhouxp
 */
@Aspect
public final class PreventReplayAspect {

    private final RedisService redisService;
    private final String keyPrefix;

    public PreventReplayAspect(RedisService redisService, String keyPrefix) {
        this.redisService = redisService;
        this.keyPrefix = keyPrefix;
    }

    /**
     * 切入点
     */
    @Pointcut("@annotation(cn.piesat.framework.redis.annotation.PreventReplay)")
    public void pointcut() {
    }

    /**
     * 处理前
     */
    @Before("pointcut()")
    public void joinPoint(JoinPoint joinPoint) throws Exception {
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            throw new BaseException("[防刷]入参不允许为空");
        }
        String requestStr = JSON.toJSONString(args[0]);
        if (!StringUtils.hasText(requestStr) || requestStr.equalsIgnoreCase("{}")) {
            throw new BaseException("[防刷]入参不允许为空");
        }

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = joinPoint.getTarget().getClass().getMethod(methodSignature.getName(),
                methodSignature.getParameterTypes());

        PreventReplay preventAnnotation = method.getAnnotation(PreventReplay.class);
        String methodFullName = method.getDeclaringClass().getName() + method.getName();
        defaultHandle(requestStr, preventAnnotation, methodFullName);
    }

    /**
     * 默认处理方式
     */
    private void defaultHandle(String requestStr, PreventReplay preventReplay, String methodFullName) throws Exception {
        String base64Str = toBase64String(requestStr);
        long expire = preventReplay.value();
        String resp = redisService.getObject(keyPrefix + "_" + methodFullName + base64Str);
        if (!StringUtils.hasText(resp)) {
            redisService.setObject(keyPrefix + "_" + methodFullName + base64Str, requestStr, expire, TimeUnit.SECONDS);
        } else {
            String message = StringUtils.hasText(preventReplay.message()) ? preventReplay.message() :
                    expire + "秒内不允许重复请求";
            throw new BaseException(message);
        }
    }


    /**
     * 对象转换为base64字符串
     *
     * @param obj 对象值
     * @return base64字符串
     */
    private String toBase64String(String obj) {
        if (StringUtils.hasText(obj)) {
            Base64.Encoder encoder = Base64.getEncoder();
            byte[] bytes = obj.getBytes(StandardCharsets.UTF_8);
            return encoder.encodeToString(bytes);
        }
        return null;
    }
}
