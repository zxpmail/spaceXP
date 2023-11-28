package cn.piesat.framework.common.model.vo;
import cn.piesat.framework.common.model.enums.CommonResponseEnum;
import cn.piesat.framework.common.model.interfaces.IBaseResponse;
import cn.piesat.framework.common.properties.CommonProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.HashMap;

/**
 * <p/>
 * {@code @description}  :统一返回值实体类
 * <p/>
 * <b>@create:</b> 2023/9/22 11:17.
 *
 * @author zhouxp
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResult<T> extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public static<T> ApiResult<T> ok(Integer code, String msg, T data) {
        ApiResult<T> apiResult = new ApiResult<>();
        apiResult.put(CommonProperties.Result.code, code);
        apiResult.put(CommonProperties.Result.message, msg);
        apiResult.put(CommonProperties.Result.data, data);
        return apiResult;
    }
    public static<T> ApiResult<T> ok(Integer code, String msg) {
        ApiResult<T> apiResult = new ApiResult<>();
        apiResult.put(CommonProperties.Result.code, code);
        apiResult.put(CommonProperties.Result.message, msg);
        return apiResult;
    }
    public static<T> ApiResult<T> ok(String msg) {
        ApiResult<T> apiResult = new ApiResult<>();
        apiResult.put(CommonProperties.Result.code, CommonResponseEnum.SUCCESS.getCode());
        apiResult.put(CommonProperties.Result.message, msg);
        return apiResult;
    }

    public static <T> ApiResult<T> ok(T data, String msg) {
        ApiResult<T> apiResult = new ApiResult<>();
        apiResult.put(CommonProperties.Result.code, CommonResponseEnum.SUCCESS.getCode());
        apiResult.put(CommonProperties.Result.data, data);
        apiResult.put(CommonProperties.Result.message, msg);
        return apiResult;
    }
    public static<T> ApiResult<T> ok() {
        ApiResult<T> apiResult = new ApiResult<>();
        apiResult.put(CommonProperties.Result.code, CommonResponseEnum.SUCCESS.getCode());
        apiResult.put(CommonProperties.Result.message, CommonResponseEnum.SUCCESS.getMessage());
        return apiResult;
    }

    public static <T> ApiResult<T> ok(T o) {
        // 支持Controller层直接返回ApiResult
        ApiResult<T> result =ApiResult.ok();
        if (!(o instanceof ApiResult)) {
            // 其他obj封装进data,保持返回格式统一
            result.put(CommonProperties.Result.data, o);
        }
        return result;
    }
    public static <T> ApiResult<T> fail(Integer code, String msg,T data) {
        return ApiResult.ok(code,msg,data);
    }

    public static <T> ApiResult<T> fail(Integer code, String msg) {
        return ApiResult.ok(code, msg);
    }
    public static<T>  ApiResult<T> fail(String msg) {
        return ApiResult.ok(CommonResponseEnum.ERROR.getCode(), msg);
    }

    public static<T>  ApiResult<T> fail() {
        return ApiResult.ok(CommonResponseEnum.ERROR.getCode(), CommonResponseEnum.ERROR.getMessage());
    }
    public static <T> ApiResult<T> fail(IBaseResponse iCommonResponse) {
        return ApiResult.ok(iCommonResponse.getCode(), iCommonResponse.getMessage());
    }

    public ApiResult<T> put(String key, T value) {
        super.put(key, value);
        return this;
    }
}
