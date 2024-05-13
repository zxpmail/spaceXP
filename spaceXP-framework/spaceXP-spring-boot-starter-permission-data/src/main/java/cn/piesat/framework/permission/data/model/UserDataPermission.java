package cn.piesat.framework.permission.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * <p/>
 * {@code @description}  :用户权限类
 * <p/>
 * <b>@create:</b> 2023/9/6 10:43.
 *
 * @author zhouxp
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserDataPermission {
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 用户名称：登录名称
     */
    private String username;

    /**
     * 用户权限范围
     */
    private Integer dataScope;
    /**
     * 用户权限部门列表 id 只能为Long类型或者String类型
     */
    private Set<Object> deptIds ;
}
