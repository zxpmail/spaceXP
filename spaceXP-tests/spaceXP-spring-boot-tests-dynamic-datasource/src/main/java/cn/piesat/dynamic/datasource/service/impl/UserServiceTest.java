package cn.piesat.dynamic.datasource.service.impl;


import cn.piesat.dynamic.datasource.model.entity.UserDO;
import cn.piesat.framework.dynamic.datasource.annotation.DS;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


@Service("UserServiceTest")
@DS("slave")
public class UserServiceTest {

    @Resource
    private UserServiceImpl1 userServiceImpl1;
    @Resource
    private UserServiceImpl userServiceImpl;

    public Object get() {
        UserDO info1 = userServiceImpl1.info(1L);
        UserDO info2 = userServiceImpl.info(1L);
        return new HashMap<String,Object>(){{put("info1",info1);put("info2",info2);}};
    }


}
