package cn.piesat.tests.mybaits.plus.service;

import cn.piesat.tests.mybaits.plus.model.entity.TestDO;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * 
 *
 * @author zhouxp
 * @email zhouxiaoping@piesat.cn
 * @date 2022-10-08 14:43:40
 */
public interface TestService extends IService<TestDO> {

    long insertUsersOneByOne();
    long insertUsersBySql();
    long saveUsersBatch();
    long insertUsersWithBatchProcessing();
    long insertUsersWithJdbcBatch();
    long insertUsersWithCustomBatch();
}

