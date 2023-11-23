package cn.piesat.framework.permission.url.enums;


import cn.piesat.framework.common.model.interfaces.IBaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <p/>
 * {@code @description}  :Url权限响应异常枚举类
 * <p/>
 * <b>@create:</b> 2023/9/12 10:26.
 *
 * @author zhouxp
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum UrlPermissionResponseEnum implements IBaseResponse {
    TOKEN_NOT_VALID(1110,"token无效！"),
    NO_PERMISSION(1111,"此接口没有权限！"),

    ;
    /**
     * 响应状态码
     */
    private  Integer code;

    /**
     * 响应信息
     */
    private  String message;
}
