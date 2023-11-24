package cn.piesat.test.permission.data.service.impl;


import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;
import cn.piesat.framework.mybatis.plus.utils.QueryUtils;

import cn.piesat.test.permission.data.dao.mapper.UserMapper;
import cn.piesat.test.permission.data.model.entity.UserDO;
import cn.piesat.test.permission.data.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
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
    public UserDO info(Long id) {
        return getById(id);
    }
    @Override
    public Boolean add(UserDO userDO) {
        return save(userDO);
    }

    @Override
    public Boolean update(UserDO userDO) {
        return updateById(userDO) ;
    }

    @Override
    public Boolean delete(List<Long> asList) {
        return removeBatchByIds(asList);
    }

    @Override
    public Boolean delete(Long id) {
        return removeById(id);
    }
}
