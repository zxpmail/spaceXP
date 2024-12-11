package cn.piesat.tests.gray.service;





import cn.piesat.tests.gray.model.entity.UserDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 
 *
 * @author zhouxp
 * @email zhouxiaoping@piesat.cn
 * @date 2022-10-08 14:43:40
 */
public interface UserService extends IService<UserDO> {

    Map<String, Object> login(String userName, String password);

}

