package cn.piesat.test.permission.url.service;

import cn.piesat.framework.permission.url.core.UserUrlPermissionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2023/11/23 17:32.
 *
 * @author zhouxp
 */
@Service
public class UserUrlPermissionServiceImpl implements UserUrlPermissionService {
    @Override
    public List<String> getUrl(Long userId) {
        return new ArrayList<String>(){{
            add("/test/get1");
        }};
    }

}
