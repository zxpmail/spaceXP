package cn.piesat.dynamic.datasource.service.impl;


import cn.piesat.dynamic.datasource.model.entity.UserDO;
import cn.piesat.framework.dynamic.datasource.annotation.DS;

import jakarta.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


import java.util.HashMap;



@Service("UserServiceTest")
@DS("slave")
public class UserServiceTest {

    @Resource
    @Qualifier("userService1")
    private UserServiceImpl1 userServiceImpl1;
    @Resource
    @Qualifier("userService")
    private UserServiceImpl userServiceImpl;

    public Object get() {
        UserDO info1 = userServiceImpl1.info(1L);
        UserDO info2 = userServiceImpl.info(1L);
        return new HashMap<String,Object>(){{put("info1",info1);put("info2",info2);}};
    }


}
