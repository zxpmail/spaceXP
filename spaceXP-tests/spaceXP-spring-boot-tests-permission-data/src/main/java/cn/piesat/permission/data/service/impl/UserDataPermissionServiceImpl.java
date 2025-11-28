package cn.piesat.permission.data.service.impl;

import cn.piesat.framework.common.utils.ServletUtils;
import cn.piesat.framework.permission.data.core.UserDataPermissionService;
import cn.piesat.framework.permission.data.model.DataPermissionEnum;
import cn.piesat.framework.permission.data.model.UserDataPermission;
import org.springframework.stereotype.Service;

import java.util.HashSet;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2023/11/24 9:23.
 *
 * @author zhouxp
 */
@Service
public class UserDataPermissionServiceImpl implements UserDataPermissionService {
    @Override
    public UserDataPermission getDataPermission(Long userid) {
        ServletUtils.getHeader(ServletUtils.getRequest(), "userId")
        UserDataPermission userDataPermission = new UserDataPermission();
        userDataPermission.setDataScope(DataPermissionEnum.SELF_SCOPE.getCode());
        userDataPermission.setUserId(userid);
        userDataPermission.setUsername("admin1");
        userDataPermission.setDeptIds(new HashSet<Object>(){{add(0L);}});
        return userDataPermission;
    }
}
