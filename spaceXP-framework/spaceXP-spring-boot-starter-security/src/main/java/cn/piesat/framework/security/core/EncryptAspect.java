package cn.piesat.framework.security.core;


import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import cn.piesat.framework.common.exception.BaseException;
import cn.piesat.framework.common.model.enums.CommonResponseEnum;
import cn.piesat.framework.common.utils.AesUtils;
import cn.piesat.framework.security.annotation.EncryptField;
import cn.piesat.framework.security.annotation.EncryptMethod;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * <p/>
 * {@code @description}  :安全字段加密解密切面
 * <p/>
 * <b>@create:</b> 2022/12/9 21:32.
 *
 * @author zhouxp
 */
@Aspect
@Slf4j
public class EncryptAspect {

    public EncryptAspect(String key,String iv) {
        AesUtils.init(key,iv);
    }

    @Pointcut("@annotation(cn.piesat.framework.security.annotation.EncryptMethod)")
    public void encryptPointCut() {
    }
    /***
     * 定义controller切入点拦截规则，拦截OpLog注解的方法
     */
    @Pointcut("@annotation(cn.piesat.framework.security.annotation.DecryptMethod)")
    public void decryptPointCut() {

    }

    @Before("encryptPointCut()")
    public void encryptBefore(JoinPoint jp) throws IllegalAccessException {
        // 先拿到被增强的方法的签名对象
        Signature signature = jp.getSignature();
        // 判断被增强的目标是否是一个方法 如果是进行强转
        assert signature instanceof MethodSignature;
        MethodSignature ms = (MethodSignature) signature;

        // 拿到目标方法
        Method method = ms.getMethod();
        // 获取method上的注解 并且拿到注解上的value值，AutoFill.class为自定义注解类
        EncryptMethod annotation = method.getAnnotation(EncryptMethod.class);
        // 如果注解为空，抛出自定义异常


        // 获取注解的值
        int pos = annotation.argsPos();

        // 如果注解为空，抛出自定义异常
        if (ObjUtil.isNull(jp.getArgs()) || ArrayUtil.isEmpty(jp.getArgs())) {
            throw new BaseException(CommonResponseEnum.AOP_ADVICE_ERROR);
        }

        // 获取参数
        Object arg = jp.getArgs()[pos];
        encrypt(arg);
    }
    /**
     *最终处理
     */
    @AfterReturning(returning = "ret", pointcut = "decryptPointCut()")
    public void decryptAfterReturning(Object ret) throws Exception {
        decrypt(ret);
    }


    /**
     * 加密
     *
     * @param requestObj 加密实体
     */
    private void encrypt(Object requestObj) throws IllegalAccessException {
        Field[] fields = requestObj.getClass().getDeclaredFields();
        for (Field field : fields) {
            boolean hasSecureField = field.isAnnotationPresent(EncryptField.class);
            if (hasSecureField) {
                field.setAccessible(true);
                String plaintextValue = (String) field.get(requestObj);
                String encryptValue = AesUtils.encrypt(plaintextValue);
                field.set(requestObj, encryptValue);
            }
        }
    }


    /**
     * 解密
     *
     * @param responseObj 解密实体
     */
    private void decrypt(Object responseObj) throws IllegalAccessException {

        Field[] fields = responseObj.getClass().getDeclaredFields();
        for (Field field : fields) {
            boolean hasSecureField = field.isAnnotationPresent(EncryptField.class);
            if (hasSecureField) {
                field.setAccessible(true);
                String encryptValue = (String) field.get(responseObj);
                String plaintextValue = AesUtils.decrypt(encryptValue);
                field.set(responseObj, plaintextValue);
            }
        }
    }
}
