package cn.piesat.framework.common.utils;

import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * <p/>
 * {@code @description}: 方法反射工具类
 * <p/>
 * {@code @create}: 2024-12-23 15:51
 * {@code @author}: zhouxp
 */
public class MethodInvocationUtil {
    private MethodInvocationUtil() {
    }

    /**
     * 获取当前方法信息
     */
    public static Method getActualMethod(MethodInvocation methodInvocation) {
        try {
            Method method = methodInvocation.getMethod();

            Object target = methodInvocation.getThis();
            assert target != null;
            return target.getClass().getMethod(method.getName(), method.getParameterTypes());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
