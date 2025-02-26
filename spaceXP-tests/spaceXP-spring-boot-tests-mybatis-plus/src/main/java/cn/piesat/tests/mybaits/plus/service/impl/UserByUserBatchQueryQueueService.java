package cn.piesat.tests.mybaits.plus.service.impl;

import cn.piesat.framework.mybatis.plus.external.core.BatchQueryQueueService;
import cn.piesat.tests.mybaits.plus.model.entity.UserDO;
import cn.piesat.tests.mybaits.plus.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * <p/>
 * {@code @description}:
 * <p/>
 * {@code @create}: 2025-02-26 10:27:13
 * {@code @author}: zhouxp
 */
@Service
public class UserByUserBatchQueryQueueService extends BatchQueryQueueService<UserDO, UserDO> {

    private final UserService userService;

    public UserByUserBatchQueryQueueService(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        super.init(userService::queryUserByUserBatch);
    }

    public UserDO getByIdUser(UserDO userDO) {
        return super.queryResult(userDO);
    }
}
