package cn.piesat.framework.security.core;


import cn.piesat.framework.security.annotation.EncryptField;
import cn.piesat.framework.security.utils.AseUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * <p/>
 * {@code @description}  :安全字段加密解密切面
 * <p/>
 * <b>@create:</b> 2022/12/9 21:32.
 *
 * @author zhouxp
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Aspect
@Slf4j
public class EncryptAspect {

    private final String secretKey;

    public EncryptAspect(String secretKey) {
        this.secretKey = secretKey;
    }

    @Pointcut("@annotation(cn.piesat.framework.security.annotation.EncryptMethod)")
    public void annotationPointCut() {
    }

    @Around("annotationPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object responseObj = null;
        Object requestObj = joinPoint.getArgs()[0];
        encrypt(requestObj);
        responseObj = joinPoint.proceed();
        decrypt(responseObj);
        return responseObj;
    }

    /**
     * 加密
     *
     * @param requestObj 加密实体
     */
    private void encrypt(Object requestObj) throws IllegalAccessException {
        if (Objects.isNull(requestObj)) {
            return;
        }
        Field[] fields = requestObj.getClass().getDeclaredFields();
        for (Field field : fields) {
            boolean hasSecureField = field.isAnnotationPresent(EncryptField.class);
            if (hasSecureField) {
                field.setAccessible(true);
                String plaintextValue = (String) field.get(requestObj);
                String encryptValue = AseUtils.encrypt(plaintextValue, secretKey);
                field.set(requestObj, encryptValue);
            }
        }
    }


    /**
     * 解密
     *
     * @param responseObj 解密实体
     */
    private Object decrypt(Object responseObj) throws IllegalAccessException {
        if (Objects.isNull(responseObj)) {
            return null;
        }

        Field[] fields = responseObj.getClass().getDeclaredFields();
        for (Field field : fields) {
            boolean hasSecureField = field.isAnnotationPresent(EncryptField.class);
            if (hasSecureField) {
                field.setAccessible(true);
                String encryptValue = (String) field.get(responseObj);
                String plaintextValue = AseUtils.decrypt(encryptValue, secretKey);
                field.set(responseObj, plaintextValue);
            }
        }
        return responseObj;
    }
}
