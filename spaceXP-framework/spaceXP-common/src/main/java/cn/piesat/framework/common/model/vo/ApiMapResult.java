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
public class ApiMapResult<T> extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public static<T> ApiMapResult<T> ok(Integer code, String msg, T data) {
        ApiMapResult<T> apiResult = new ApiMapResult<>();
        apiResult.put(CommonProperties.Result.code, code);
        apiResult.put(CommonProperties.Result.message, msg);
        apiResult.put(CommonProperties.Result.data, data);
        return apiResult;
    }
    public static<T> ApiMapResult<T> ok(Integer code, String msg) {
        ApiMapResult<T> apiResult = new ApiMapResult<>();
        apiResult.put(CommonProperties.Result.code, code);
        apiResult.put(CommonProperties.Result.message, msg);
        return apiResult;
    }
    public static<T> ApiMapResult<T> ok(String msg) {
        ApiMapResult<T> apiResult = new ApiMapResult<>();
        apiResult.put(CommonProperties.Result.code, CommonResponseEnum.SUCCESS.getCode());
        apiResult.put(CommonProperties.Result.message, msg);
        return apiResult;
    }

    public static <T> ApiMapResult<T> ok(T data, String msg) {
        ApiMapResult<T> apiResult = new ApiMapResult<>();
        apiResult.put(CommonProperties.Result.code, CommonResponseEnum.SUCCESS.getCode());
        apiResult.put(CommonProperties.Result.data, data);
        apiResult.put(CommonProperties.Result.message, msg);
        return apiResult;
    }
    public static<T> ApiMapResult<T> ok() {
        ApiMapResult<T> apiResult = new ApiMapResult<>();
        apiResult.put(CommonProperties.Result.code, CommonResponseEnum.SUCCESS.getCode());
        apiResult.put(CommonProperties.Result.message, CommonResponseEnum.SUCCESS.getMessage());
        return apiResult;
    }

    public static <T> ApiMapResult<T> ok(T o) {
        // 支持Controller层直接返回ApiResult
        ApiMapResult<T> result = ApiMapResult.ok();
        if (!(o instanceof ApiMapResult)) {
            // 其他obj封装进data,保持返回格式统一
            result.put(CommonProperties.Result.data, o);
        }
        return result;
    }
    public static <T> ApiMapResult<T> fail(Integer code, String msg, T data) {
        return ApiMapResult.ok(code,msg,data);
    }

    public static <T> ApiMapResult<T> fail(Integer code, String msg) {
        return ApiMapResult.ok(code, msg);
    }
    public static<T> ApiMapResult<T> fail(String msg) {
        return ApiMapResult.ok(CommonResponseEnum.ERROR.getCode(), msg);
    }

    public static<T> ApiMapResult<T> fail() {
        return ApiMapResult.ok(CommonResponseEnum.ERROR.getCode(), CommonResponseEnum.ERROR.getMessage());
    }
    public static <T> ApiMapResult<T> fail(IBaseResponse iCommonResponse) {
        return ApiMapResult.ok(iCommonResponse.getCode(), iCommonResponse.getMessage());
    }

    public ApiMapResult<T> put(String key, T value) {
        super.put(key, value);
        return this;
    }
}
