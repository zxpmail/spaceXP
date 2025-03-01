package cn.piesat.framework.common.exception;

import cn.piesat.framework.common.model.enums.CommonResponseEnum;
import cn.piesat.framework.common.model.interfaces.IBaseResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * <p/>
 * {@code @description}  :基础异常处理类
 * <p/>
 * <b>@create:</b> 2023/9/25 11:09.
 *
 * @author zhouxp
 */
@Getter
@Setter
public class BaseException extends RuntimeException{

    /**
     * 对基础响应接口进行处理
     */
    private final IBaseResponse iBaseResponse;


    public BaseException(Throwable cause, IBaseResponse iBaseResponse) {
        super(iBaseResponse.getMessage(),cause);
        this.iBaseResponse = iBaseResponse;
    }

    /**
     * 输入iBaseResponse接口子类进行处理
     */
    public BaseException(IBaseResponse iBaseResponse) {
        super(iBaseResponse.getMessage());
        this.iBaseResponse = iBaseResponse;
    }

    /**
     * 输入字符串消息构造函数进行处理
     * @param message 消息字符串
     */
    public BaseException(String message) {
        super(message);
        this.iBaseResponse = new IBaseResponse() {
            @Override
            public Integer getCode() {
                return CommonResponseEnum.SYS_ERROR.getCode();
            }

            @Override
            public String getMessage() {
                return message;
            }
        };
    }

    public static void errorResponse(IBaseResponse iBaseResponse)  {
            throw  exception(iBaseResponse);

    }
    public static RuntimeException exception(IBaseResponse iBaseResponse) {
        if (iBaseResponse == null) {
            return new IllegalArgumentException("iBaseResponse cannot be null");
        }
        return new BaseException( iBaseResponse);
    }
}
