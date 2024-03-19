package cn.piesat.tools.gateway.enums;

import cn.piesat.framework.common.model.interfaces.IBaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <p/>
 * {@code @description}  :gateway响应
 * <p/>
 * <b>@create:</b> 2024-03-19 9:12.
 *
 * @author zhouxp
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum GatewayResponseEnum implements IBaseResponse {
    REDIRECT(50000,"重定向"),
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
