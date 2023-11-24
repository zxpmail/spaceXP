package cn.piesat.framework.permission.data.core;


import cn.piesat.framework.permission.data.model.UserDataPermission;

/**
 * <p/>
 * {@code @description}  :获取用户权限接口类
 * <p/>
 * <b>@create:</b> 2023/9/6 10:59.
 *
 * @author zhouxp
 */
public interface UserDataPermissionService {
    /**
     * 根据用户id获取用户数据权限信息
     * @param userid 用户ID
     * @return 用户数据权限信息
     */
    UserDataPermission getDataPermission(Long userid);
}
