package cn.piesat.tools.gateway.model.enums;

import cn.piesat.framework.common.model.interfaces.IBaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhouxp
 */

@Getter
@AllArgsConstructor
public enum GatewayResponseEnum implements IBaseResponse {
    /**
     * 没有菜单权限
     */
    NO_MENU_PERMISSION(1000,"没有权限"),
    /**
     * 重定向操作
     */
    REDIRECT(50000,"重定向"),
    ;
    /**
    响应状态码
     */
    private final Integer code;
    /**
     * 响应信息
     */
    private final String message;
}
