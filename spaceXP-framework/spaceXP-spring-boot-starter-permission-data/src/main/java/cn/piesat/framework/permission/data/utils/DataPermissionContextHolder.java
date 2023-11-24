package cn.piesat.framework.permission.data.utils;

import cn.piesat.framework.permission.data.model.UserDataPermission;
import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * <p/>
 * {@code @description}  : 用户权限线程上下文使用
 * <p/>
 * <b>@create:</b> 2023/10/12 13:43.
 *
 * @author zhouxp
 */
public class DataPermissionContextHolder {
    private static final ThreadLocal<UserDataPermission> CONTEXT = new TransmittableThreadLocal<>();

    public static void setUserDataPermission(UserDataPermission userDataPermission) {
        CONTEXT.set(userDataPermission);
    }

    public static UserDataPermission getUserDataPermission() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}

