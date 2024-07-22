package cn.piesat.framework.web.core;

import cn.piesat.framework.common.annotation.NoApiResult;
import cn.piesat.framework.common.constants.CommonConstants;
import cn.piesat.framework.common.exception.BaseException;
import cn.piesat.framework.common.model.enums.CommonResponseEnum;
import cn.piesat.framework.common.model.interfaces.IBaseResponse;
import cn.piesat.framework.common.model.vo.ApiMapResult;
import cn.piesat.framework.common.utils.ExceptionUtil;
import feign.FeignException;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Set;


/**
 * <p/>
 * {@code @description}  :统一异常处理类
 * <p/>
 * <b>@create:</b> 2023/9/27 8:26.
 *
 * @author zhouxp
 */
@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class WebExceptionHandler {

    private final String module;

    /**
     * NoHandlerFoundException 404 异常处理
     */
    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiMapResult<IBaseResponse> handlerNoHandlerFoundException(NoHandlerFoundException e) throws Throwable {
        errorDispose(e);
        outPutErrorWarn(NoHandlerFoundException.class, CommonResponseEnum.NOT_FOUND, e);
        return ApiMapResult.fail(CommonResponseEnum.NOT_FOUND);
    }


    /**
     * 处理BaseException子类异常
     *
     * @param e BaseException及子类
     * @return ApiResult包装异常
     */
    @ExceptionHandler(value = BaseException.class)
    //@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) 前端blob时有些问题，暂时取得此注释
    public Object handleBusinessException(BaseException e) throws Throwable {
        errorDispose(e);
        log.error(ExceptionUtil.getMessage(e));
        if (e.getIBaseResponse() == null) {
            return ApiMapResult.fail(e.getMessage());
        }
        log.error(CommonConstants.MESSAGE, module, e.getIBaseResponse().getMessage());
        return ApiMapResult.fail(e.getIBaseResponse());
    }

    /**
     * Exception 类捕获 500 异常处理
     */
    @ExceptionHandler(value = Exception.class)
    public ApiMapResult<IBaseResponse> handlerException(Exception e) throws Throwable {
        errorDispose(e);
        return ifDepthExceptionType(e);
    }

    /**
     * 二次深度检查错误类型
     */
    private ApiMapResult<IBaseResponse> ifDepthExceptionType(Throwable throwable) throws Throwable {
        Throwable cause = throwable.getCause();
        if (cause instanceof FeignException) {
            return handlerFeignException((FeignException) cause);
        }
        outPutError(Exception.class, CommonResponseEnum.ERROR, throwable);
        if (cause == null) {
            return ApiMapResult.fail(throwable.getMessage());
        }
        return ApiMapResult.fail(cause.getMessage());
    }

    /**
     * FeignException 类捕获
     */
    @ExceptionHandler(value = FeignException.class)
    public ApiMapResult<IBaseResponse> handlerFeignException(FeignException e) throws Throwable {
        errorDispose(e);
        outPutError(FeignException.class, CommonResponseEnum.RPC_ERROR, e);
        return ApiMapResult.fail(CommonResponseEnum.RPC_ERROR);
    }

    /**
     * HttpRequestMethodNotSupportedException 405 异常处理
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiMapResult<IBaseResponse> handlerHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) throws Throwable {
        errorDispose(e);
        outPutErrorWarn(HttpRequestMethodNotSupportedException.class,
                CommonResponseEnum.METHOD_NOT_ALLOWED, e);
        return ApiMapResult.fail(CommonResponseEnum.METHOD_NOT_ALLOWED);
    }

    /**
     * HttpMediaTypeNotSupportedException 415 异常处理
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ApiMapResult<IBaseResponse> handlerHttpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException e) throws Throwable {
        errorDispose(e);
        outPutErrorWarn(HttpMediaTypeNotSupportedException.class,
                CommonResponseEnum.UNSUPPORTED_MEDIA_TYPE, e);
        return ApiMapResult.fail(CommonResponseEnum.UNSUPPORTED_MEDIA_TYPE);
    }

    /**
     * HttpMessageNotReadableException 参数错误异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiMapResult<IBaseResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) throws Throwable {
        errorDispose(e);
        outPutError(HttpMessageNotReadableException.class, CommonResponseEnum.PARAM_ERROR, e);
        String msg = String.format("%s : 错误详情( %s )", CommonResponseEnum.PARAM_ERROR.getMessage(),
                Objects.requireNonNull(e.getRootCause()).getMessage());
        return ApiMapResult.fail(CommonResponseEnum.PARAM_ERROR.getCode(), msg);
    }


    /**
     * ConstraintViolationException 参数错误异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiMapResult<IBaseResponse> handleConstraintViolationException(ConstraintViolationException e) throws Throwable {
        errorDispose(e);
        String smg = "";
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        if (log.isDebugEnabled()) {
            for (ConstraintViolation<?> error : constraintViolations) {
                log.error("{} -> {}", error.getPropertyPath(), error.getMessageTemplate());
                smg = error.getMessageTemplate();
            }
        }

        if (constraintViolations.isEmpty()) {
            log.error("validExceptionHandler error fieldErrors is empty");
            ApiMapResult.fail(CommonResponseEnum.BUSINESS_ERROR.getCode(), "");
        }

        return ApiMapResult.fail(CommonResponseEnum.PARAM_ERROR.getCode(), smg);
    }

    /**
     * MethodArgumentNotValidException 参数错误异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiMapResult<IBaseResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) throws Throwable {
        errorDispose(e);
        BindingResult bindingResult = e.getBindingResult();
        return getBindResult(bindingResult);
    }

    /**
     * BindException 参数错误异常
     */
    @ExceptionHandler(BindException.class)
    public ApiMapResult<IBaseResponse> handleBindException(BindException e) throws Throwable {
        errorDispose(e);
        outPutError(BindException.class, CommonResponseEnum.PARAM_ERROR, e);
        BindingResult bindingResult = e.getBindingResult();
        return getBindResult(bindingResult);
    }

    private ApiMapResult<IBaseResponse> getBindResult(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (log.isDebugEnabled()) {
            for (FieldError error : fieldErrors) {
                log.error("{} -> {}", error.getDefaultMessage(), error.getDefaultMessage());
            }
        }

        if (fieldErrors.isEmpty()) {
            log.error("validExceptionHandler error fieldErrors is empty");
            ApiMapResult.fail(CommonResponseEnum.BUSINESS_ERROR.getCode(), "");
        }

        return ApiMapResult.fail(CommonResponseEnum.PARAM_ERROR.getCode(), fieldErrors.get(0).getDefaultMessage());
    }

    /**
     * 处理Controller层相关异常
     */
    @ExceptionHandler({
            MissingPathVariableException.class,
            MissingServletRequestParameterException.class,
            TypeMismatchException.class,
            HttpMessageNotWritableException.class,
            HttpMediaTypeNotAcceptableException.class,
            ServletRequestBindingException.class,
            ConversionNotSupportedException.class,
            MissingServletRequestPartException.class,
            AsyncRequestTimeoutException.class
    })
    public ApiMapResult<IBaseResponse> handleServletException(Exception e) {
        log.error(CommonConstants.MESSAGE, module, ExceptionUtil.getMessage(e));
        return ApiMapResult.fail(e.getMessage());
    }


    /**
     * 校验是否进行异常处理
     *
     * @param e   异常
     * @param <T> extends Throwable
     * @throws Throwable 异常
     */
    private <T extends Throwable> void errorDispose(T e) throws Throwable {

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return;
        }
        HttpServletRequest request = requestAttributes.getRequest();
        HandlerMethod handlerMethod = (HandlerMethod) request.getAttribute("org.springframework.web.servlet.HandlerMapping.bestMatchingHandler");
        if (handlerMethod == null) {
            return;
        }
        // 获取异常 Controller
        Class<?> beanType = handlerMethod.getBeanType();
        // 获取异常方法
        Method method = handlerMethod.getMethod();

        NoApiResult classAnnotation = beanType.getAnnotation(NoApiResult.class);
        NoApiResult methodAnnotation = method.getAnnotation(NoApiResult.class);

        if (classAnnotation != null && !classAnnotation.value()) {
            throw e; // 如果类上存在NoApiResult注解且value为false，则抛出异常
        }
        if (methodAnnotation != null && !methodAnnotation.value()) {
            throw e; // 如果方法上存在NoApiResult注解且value为false，则抛出异常
        }
    }

    public void outPutError(Class<?> errorType, Enum<?> secondaryErrorType, Throwable throwable) {
        log.error("[{}: {}] {}: {}", module, errorType.getSimpleName(), secondaryErrorType, throwable.getMessage(),
                throwable);
    }

    public void outPutErrorWarn(Class<?> errorType, Enum<?> secondaryErrorType, Throwable throwable) {
        log.warn("[{}: {}] {}: {}", module, errorType.getSimpleName(), secondaryErrorType, throwable.getMessage());
    }
}
