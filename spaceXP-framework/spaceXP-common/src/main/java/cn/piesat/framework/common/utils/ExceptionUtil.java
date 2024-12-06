package cn.piesat.framework.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;

/**
 * <p/>
 * {@code @description}  :异常工具类
 * <p/>
 * <b>@create:</b> 2023/12/19 13:42.
 *
 * @author zhouxp
 */
@Slf4j
public class ExceptionUtil {
    private ExceptionUtil() {
    }

    /**
     * 将ErrorStack转化为String.
     */
    public static String getMessage(Throwable ex) {
        try (StringWriter stringWriter = new StringWriter();
             PrintWriter printWriter = new PrintWriter(stringWriter)) {
            ex.printStackTrace(printWriter);
            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error while getting stack trace as string", e);
        }
    }

    /**
     * 获取组合本异常信息与底层异常信息的异常描述, 适用于本异常为统一包装异常类，底层异常才是根本原因的情况。
     */
    public static String getErrorMessageWithNestedException(Throwable ex) {
        Throwable nestedException = ex.getCause();
        return ex.getMessage() + " nested exception is " +
                nestedException.getClass().getName() + ":" + nestedException.getMessage();
    }

    /**
     * 获取异常的Root Cause.
     */
    public static Throwable getRootCause(Throwable ex) {
        Throwable cause;
        Throwable result = null;
        while ((cause = ex.getCause()) != null) {
            result = cause;
        }
        return result;
    }


    /**
     * 获取确切的异常信息：主要针对代理报错
     */
    public static Throwable getActualThrowable(final Throwable throwable) {
        if (InvocationTargetException.class.equals(throwable.getClass())) {
            InvocationTargetException exception = (InvocationTargetException) throwable;
            return exception.getTargetException();
        }
        return throwable;
    }
}
