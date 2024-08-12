package cn.piesat.framework.common.aspect;

import cn.piesat.framework.common.annotation.ServiceValidation;
import cn.piesat.framework.common.constants.CommonConstants;
import cn.piesat.framework.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * 校验service 中JSR303
 * <p/>
 * {@code @description}  :拦截含有ServiceValidation注解AOP
 * <p/>
 * <b>@create:</b> 2024/08/09 15:09.
 *
 * @author zhouxp
 */
@Slf4j
@Aspect
public class ServiceValidationAspect {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /***
     * 定义service切入点拦截规则，拦截ServiceValidation注解的方法
     */
    @Pointcut("@annotation(cn.piesat.framework.common.annotation.ServiceValidation)")
    public void serviceValidationAspect() {

    }

    /***
     * 拦截控制层的操作日志
     */
    @Before(value = "serviceValidationAspect()")
    public void validateParameters(JoinPoint joinPoint) {
        Object[] arguments = joinPoint.getArgs();
        Signature sig = joinPoint.getSignature();
        if (sig == null) {
            throw new IllegalArgumentException(CommonConstants.SIGNATURE_ARGUMENT);
        }
        MethodSignature methodSig = (MethodSignature) sig;
        Method method = methodSig.getMethod();
        String[] paramNames = methodSig.getParameterNames();
        if (paramNames == null || paramNames.length == 0) {
            return;
        }

        Annotation[][] paramAnnotations = method.getParameterAnnotations();
        if (paramAnnotations.length == 0) {
            return;
        }

        Map<Integer, Set<ConstraintViolation<Object>>> violationsMap = new HashMap<>();
        for (int i = 0; i < arguments.length; i++) {
            if (paramAnnotations[i].length > 0) {
                for (Annotation annotation : paramAnnotations[i]) {
                    if (annotation instanceof ServiceValidation) {

                        if (arguments[i] == null) {
                            throw new BaseException(((ServiceValidation) annotation).message());
                        }
                    }
                    int finalI = i;
                    Set<ConstraintViolation<Object>> validate = violationsMap.computeIfAbsent(i, k -> validator.validate(arguments[finalI]));
                    if (!validate.isEmpty()) {
                        throw new ConstraintViolationException(validate);
                    }
                }
            }
        }
    }
}
