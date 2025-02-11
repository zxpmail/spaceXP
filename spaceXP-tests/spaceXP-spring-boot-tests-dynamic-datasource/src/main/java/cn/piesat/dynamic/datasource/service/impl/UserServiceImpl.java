package cn.piesat.dynamic.datasource.service.impl;

import cn.piesat.dynamic.datasource.dao.mapper.UserMapper;
import cn.piesat.dynamic.datasource.model.entity.UserDO;
import cn.piesat.dynamic.datasource.service.UserService;
import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;
import cn.piesat.framework.dynamic.datasource.annotation.DS;
import cn.piesat.framework.mybatis.plus.annotation.DynamicTableName;
import cn.piesat.framework.mybatis.plus.utils.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service("userService")
//@DS("slave")
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {


    @Override
    public PageResult list(PageBean pageBean, UserDO userDO) {
        QueryWrapper<UserDO> wrapper = new QueryWrapper<>(userDO);
        IPage<UserDO> page = this.page(
                QueryUtils.getPage(pageBean),
                wrapper
        );
        return new PageResult(page.getTotal(), page.getRecords());
    }


    @Override
    public Boolean addMaster(UserDO userDO) {
        return save(userDO);
    }

    @Override
    public Boolean addSlave(UserDO userDO) {
        return save(userDO);
    }

    @Override
    public Boolean addNested(UserDO userDO) {
        save(userDO);
        return userService.addMaster(userDO);
    }

    @Transactional
    @Override
    public Boolean addTrans(UserDO userDO) {
        userService.addMaster(userDO);
        userService.addSlave(userDO);
        return true;
    }


    @DS
    @Async
    @Override
    public void addTest1() {
        UserDO userDO = new UserDO();
        for (long i = 0; i < 100000; i++) {
            userDO.setId(null);
            userDO.setName(1 + " :master");
            save(userDO);
        }
    }

    @DS("slave")
    @DynamicTableName
    @Async
    @Override
    public void addTest2() {
        UserDO userDO = new UserDO();
        for (long i = 0; i < 100000; i++) {
            userDO.setId(null);
            userDO.setName(1 + " :slave");
            save(userDO);
        }
    }

    @DS
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void addTestXa1() {
        UserDO userDO = new UserDO();
        userDO.setId(null);
        userDO.setName(1 + " :master");
        save(userDO);
    }

    @DS("slave")
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addTestXa2() {
        UserDO userDO = new UserDO();
        userDO.setId(null);
        userDO.setName(1 + " :slave");
        save(userDO);
    }

    @Lazy
    @Autowired(required = false)
    private UserService userService;
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
    @Override
    public void execXa() {
        userService.addTestXa1();
        userService.addTestXa2();
    }


}
