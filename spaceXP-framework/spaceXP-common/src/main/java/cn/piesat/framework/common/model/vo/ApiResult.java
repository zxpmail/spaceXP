package cn.piesat.framework.common.model.vo;


import cn.piesat.framework.common.model.enums.CommonResponseEnum;
import cn.piesat.framework.common.model.interfaces.IBaseResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * <p/>
 * {@code @description}  :统一返回值实体类
 * <p/>
 * <b>@create:</b> 2023/9/22 11:17.
 *
 * @author zhouxp
 */

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResult <T>{
    /**
     * 返回代码
     */
    private Integer code;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    private ApiResult() {
        this.code = CommonResponseEnum.SUCCESS.getCode();
        this.message = CommonResponseEnum.SUCCESS.getMessage();
    }
    private ApiResult(T data) {
        this.code = CommonResponseEnum.SUCCESS.getCode();
        this.message = CommonResponseEnum.SUCCESS.getMessage();
        this.data = data;
    }

    private ApiResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private ApiResult(IBaseResponse iCommonResponse) {
        this.code = iCommonResponse.getCode();
        this.message = iCommonResponse.getMessage();
    }

    public static<E> ApiResult<E> ok() {
        return new ApiResult<>();
    }

    public static <E> ApiResult<E> ok(E o) {
        // 支持Controller层直接返回ApiResult
        ApiResult<E> result = new ApiResult<>(CommonResponseEnum.SUCCESS);

        if (!(o instanceof ApiResult)) {
            // 其他obj封装进data,保持返回格式统一
            result.setData(o);
        }
        return result;
    }

    public static<T> ApiResult<T> ok(String msg) {
        return new ApiResult<>(CommonResponseEnum.SUCCESS.getCode(), msg, null);
    }

    public static <T> ApiResult<T> ok(T data, String msg) {
        return new ApiResult<>(CommonResponseEnum.SUCCESS.getCode(), msg, data);
    }

    /**
     * 自定义返回码
     */
    public static <T>ApiResult<T> ok(Integer code, String msg) {
        return new ApiResult<>(code, msg);
    }


    /**
     * 自定义
     *
     * @param code 验证码
     * @param msg  返回消息内容
     * @param data 返回数据
     * @return 响应体
     */
    public static<T> ApiResult<T> ok(Integer code, String msg, T data) {
        return new ApiResult<>(code, msg, data);
    }

    public static <T> ApiResult<T> fail() {
        return new ApiResult<>(CommonResponseEnum.ERROR.getCode(), null, null);
    }
    public static <T> ApiResult<T> fail(IBaseResponse iCommonResponse) {
        return new ApiResult<>(iCommonResponse);
    }

    /***
     * 自定义错误返回码
     *
     * @param msg 消息内容
     * @return 响应体
     */
    public static <T> ApiResult<T> fail(String msg) {
        return new ApiResult<>(CommonResponseEnum.ERROR.getCode(), msg, null);
    }


    /***
     * 自定义错误返回码
     *
     * @param code 验证码
     * @param msg 消息内容
     * @return 响应体
     */
    public static <T> ApiResult<T> fail(Integer code, String msg) {
        return new ApiResult<>(code, msg, null);
    }

    /***
     * 自定义错误返回码
     *
     * @param code 验证码
     * @param msg 消息内容
     * @return 响应体
     */
    public static <T> ApiResult<T> fail(Integer code, String msg,T data) {
        return new ApiResult<>(code, msg, data);
    }

}
