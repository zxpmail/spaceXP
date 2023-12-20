package cn.piesat.framework.web.core;

import cn.piesat.framework.common.constants.CommonConstants;
import cn.piesat.framework.common.exception.BaseException;
import cn.piesat.framework.common.model.enums.CommonResponseEnum;
import cn.piesat.framework.common.model.interfaces.IBaseResponse;
import cn.piesat.framework.common.model.vo.ApiResult;
import cn.piesat.framework.web.enums.WebResponseEnum;
import cn.piesat.framework.web.utils.ExceptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
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
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;


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
    public Object handleBusinessException(BaseException e) {
        log.error(ExceptionUtil.getMessage(e));
        if (e.getIBaseResponse() == null) {
            return ApiResult.fail(e.getMessage());
        }
        log.error(CommonConstants.MESSAGE, module, e.getIBaseResponse().getMessage());
        return ApiResult.fail(e.getIBaseResponse());
    }

    /**
     * 其他异常处理
     * @param e Exception除了处理外的其他异常
     * @return ApiResult包装异常
     */
    @ExceptionHandler(value = Exception.class)
    public ApiResult<IBaseResponse> handleException(Exception e) {
        log.error(CommonConstants.MESSAGE, module, ExceptionUtil.getMessage(e));
        return ApiResult.fail(e.getMessage());
    }


    /**
     * 处理HttpMessage不能读异常
     * @param e HttpMessageNotReadableException
     * @return ApiResult包装异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResult<IBaseResponse> handleDateTimeParseException(HttpMessageNotReadableException e) {
        log.error(CommonConstants.MESSAGE, module, ExceptionUtil.getMessage(e));
        return ApiResult.fail( e.getMessage());
    }

    /**
     * 处理输入参数校验异常
     * @param e 校验异常
     * @return ApiResult包装异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResult<IBaseResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(CommonConstants.MESSAGE, module, ExceptionUtil.getMessage(e));
        BindingResult bindingResult = e.getBindingResult();
        if (bindingResult.hasErrors()){
            StringBuffer errors = new StringBuffer();
            bindingResult.getAllErrors().forEach(error ->{
                errors.append(error.getDefaultMessage()).append(";");
            });
            return ApiResult.fail(errors.toString());
        }
        return ApiResult.fail(WebResponseEnum.INVALID_INPUT);
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
    public ApiResult<IBaseResponse> handleServletException(Exception e) {
        log.error(CommonConstants.MESSAGE, module, ExceptionUtil.getMessage(e));
        return ApiResult.fail(e.getMessage());
    }
}
