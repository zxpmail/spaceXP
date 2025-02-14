package cn.piesat.dynamic.datasource.service.impl;

import cn.piesat.dynamic.datasource.dao.mapper.UserMapper;
import cn.piesat.dynamic.datasource.model.entity.UserDO;
import cn.piesat.dynamic.datasource.service.UserService;
import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;

import cn.piesat.framework.dynamic.datasource.annotation.DSTransactional;
import cn.piesat.framework.mybatis.plus.utils.QueryUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import org.springframework.stereotype.Service;



@Service("userService")
//@DS("slave")
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

        @Lazy
    @Autowired(required = false)
    private UserService userService;
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

    //@DSTransactional
    @Override
    public Boolean addTrans(UserDO userDO) {
        userService.addMaster(userDO);
        int i= 1/0;
        userService.addSlave(userDO);
        return true;
    }
}
