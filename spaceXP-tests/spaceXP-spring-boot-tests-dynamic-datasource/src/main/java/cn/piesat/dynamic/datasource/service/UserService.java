package cn.piesat.dynamic.datasource.service;


import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;
import cn.piesat.dynamic.datasource.model.entity.UserDO;
import cn.piesat.framework.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.IService;


import java.util.List;

/**
 * 用户信息Service接口
 *
 * @author zhouxp
 * @email zhouxiaoping@piesat.cn
 * @date 2023-01-16 08:52:49
 */
public interface UserService extends IService<UserDO> {

    PageResult list(PageBean pageBean, UserDO userDO);

    UserDO info(Long id);

    Boolean add(UserDO userDO);

    Boolean update(UserDO userDO);

    Boolean delete(List<Long> asList);

    Boolean delete(Long id);


    void addTest1();
    void addTest2();

    void addTestXa1();
    void addTestXa2();

    void execXa();
}

