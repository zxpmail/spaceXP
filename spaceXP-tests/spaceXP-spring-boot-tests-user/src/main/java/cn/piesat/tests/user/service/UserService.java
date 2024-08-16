package cn.piesat.tests.user.service;


import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;
import cn.piesat.tests.user.model.entity.UserDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author zhouxp
 * @email zhouxiaoping@piesat.cn
 * @date 2022-10-08 14:43:40
 */
public interface UserService extends IService<UserDO> {

    PageResult list(PageBean pageBean, UserDO userDO);

    UserDO info(Long id);

    Boolean add(UserDO userDO);

    Boolean update(UserDO userDO);

    Boolean delete(List<Long> asList);

    Boolean delete(Long id);

    Map<String, Object> login(String userName, String password);

    Boolean logout(Long userId);
}

