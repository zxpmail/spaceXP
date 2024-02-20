package cn.piesat.framework.web.core;

import cn.piesat.framework.common.annotation.NoApiResult;
import cn.piesat.framework.common.constants.CommonConstants;
import cn.piesat.framework.common.exception.BaseException;
import cn.piesat.framework.common.model.enums.CommonResponseEnum;
import cn.piesat.framework.common.model.interfaces.IBaseResponse;
import cn.piesat.framework.common.model.vo.ApiMapResult;
import cn.piesat.framework.web.enums.WebResponseEnum;
import cn.piesat.framework.common.utils.ExceptionUtil;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindingResult;
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

import java.lang.reflect.Method;
import java.util.Objects;


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

    private  final  String module;

    /**
     * 处理BaseException子类异常
     * @param e BaseException及子类
     * @return ApiResult包装异常
     */
    @ExceptionHandler(value = BaseException.class)
    //@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) 前端blob时有些问题，暂时取得此注释
    public Object handleBusinessException(BaseException e) {
        log.error(ExceptionUtil.getMessage(e));
        if (e.getIBaseResponse() == null) {
            return ApiMapResult.fail(e.getMessage());
        }
        log.error(CommonConstants.MESSAGE, module, e.getIBaseResponse().getMessage());
        return ApiMapResult.fail(e.getIBaseResponse());
    }

    /**
     * 其他异常处理
     * @param e Exception除了处理外的其他异常
     * @return ApiResult包装异常
     */
    @ExceptionHandler(value = Exception.class)
    public ApiMapResult<IBaseResponse> handleException(Exception e) {
        log.error(CommonConstants.MESSAGE, module, ExceptionUtil.getMessage(e));
        return ApiMapResult.fail(e.getMessage());
    }


    /**
     * 处理HttpMessage不能读异常
     * @param e HttpMessageNotReadableException
     * @return ApiResult包装异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiMapResult<IBaseResponse> handleDateTimeParseException(HttpMessageNotReadableException e) {
        log.error(CommonConstants.MESSAGE, module, ExceptionUtil.getMessage(e));
        return ApiMapResult.fail( e.getMessage());
    }

    /**
     * 处理输入参数校验异常
     * @param e 校验异常
     * @return ApiResult包装异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiMapResult<IBaseResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(CommonConstants.MESSAGE, module, ExceptionUtil.getMessage(e));
        BindingResult bindingResult = e.getBindingResult();
        if (bindingResult.hasErrors()){
            StringBuffer errors = new StringBuffer();
            bindingResult.getAllErrors().forEach(error ->{
                errors.append(error.getDefaultMessage()).append(";");
            });
            return ApiMapResult.fail(errors.toString());
        }
        return ApiMapResult.fail(WebResponseEnum.INVALID_INPUT);
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
     * 处理Controller层相关异常
     */
    @ExceptionHandler({
            NoHandlerFoundException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
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
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        HandlerMethod handlerMethod = (HandlerMethod) request.getAttribute("org.springframework.web.servlet.HandlerMapping.bestMatchingHandler");

        // 获取异常 Controller
        Class<?> beanType = handlerMethod.getBeanType();
        // 获取异常方法
        Method method = handlerMethod.getMethod();

        // 判断方法是否存在 NoApiResult 注解
        NoApiResult methodAnnotation = method.getAnnotation(NoApiResult.class);
        if (methodAnnotation != null) {
            // 是否使用异常处理
            if (!methodAnnotation.value()) {
                throw e;
            } else {
                return;
            }
        }
        // 判类是否存在 NoApiResult 注解
        NoApiResult classAnnotation = beanType.getAnnotation(NoApiResult.class);
        if (classAnnotation != null) {
            if (!classAnnotation.value()) {
                throw e;
            }
        }
    }

    public void outPutError(Class<?> errorType, Enum<?> secondaryErrorType, Throwable throwable) {
        log.error("[{}] {}: {}", errorType.getSimpleName(), secondaryErrorType, throwable.getMessage(),
                throwable);
    }

    public void outPutErrorWarn(Class errorType, Enum secondaryErrorType, Throwable throwable) {
        log.warn("[{}] {}: {}", errorType.getSimpleName(), secondaryErrorType, throwable.getMessage());
    }
}
