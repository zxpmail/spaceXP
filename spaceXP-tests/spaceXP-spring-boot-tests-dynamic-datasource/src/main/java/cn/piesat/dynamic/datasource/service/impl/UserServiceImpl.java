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
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
@DS("slave")
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService  {


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
    @DS
    @DynamicTableName
    public UserDO info(Long id) {
        throw new RuntimeException("测试！");
        //return getById(id);
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
