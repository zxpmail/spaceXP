package cn.piesat.tests.mybaits.plus.service.impl;

import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;
import cn.piesat.framework.mybatis.plus.annotation.DynamicTableName;
import cn.piesat.framework.mybatis.plus.model.TableNameEntity;
import cn.piesat.framework.mybatis.plus.utils.CheckRecordRepeatUtils;
import cn.piesat.framework.mybatis.plus.utils.QueryUtils;
import cn.piesat.tests.mybaits.plus.dao.mapper.UserMapper;
import cn.piesat.tests.mybaits.plus.model.entity.UserDO;
import cn.piesat.tests.mybaits.plus.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
        CheckRecordRepeatUtils.checkRecordRepeat(userDO.getUsername(),UserDO::getUsername,this::count);
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

    @DynamicTableName
    @Override
    public UserDO dynamicInfo(TableNameEntity tableName, Long id) {
        return getById(id);
    }

    @Override
    public UserDO getUserByName(String name) {
        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(name), UserDO::getUsername,name);
        return getOne(wrapper);
    }
}