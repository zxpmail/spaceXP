package cn.piesat.tests.mybaits.plus.service.impl;

import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;
import cn.piesat.framework.mybatis.plus.annotation.DynamicTableName;
import cn.piesat.framework.mybatis.plus.external.core.WrapRequest;
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
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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

    @Override
    public Map<String, UserDO> queryUserByIdBatch(List<WrapRequest<Long, UserDO>> userReqs) {
        // 全部参数
        List<Long> userIds = userReqs.stream().map(WrapRequest::getParams).collect(Collectors.toList());
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
        // 用in语句合并成一条SQL，避免多次请求数据库的IO
        queryWrapper.in("id", userIds);
        List<UserDO> users = list(queryWrapper);
        Map<Long, List<UserDO>> userGroup = users.stream().collect(Collectors.groupingBy(UserDO::getId));
        HashMap<String, UserDO> result = new HashMap<>();
        for (WrapRequest<Long, UserDO> val : userReqs) {
            List<UserDO> usersList = userGroup.get(val.getParams());
            if (!CollectionUtils.isEmpty(usersList)) {
                result.put(val.getRequestId(), usersList.get(0));
            } else {
                // 表示没数据
                result.put(val.getRequestId(), null);
            }
        }
        return result;
    }

    @Override
    public Map<String, UserDO> queryUserByUserBatch(List<WrapRequest<UserDO, UserDO>> userReqs) {
        // 全部参数
        List<Long> userIds = userReqs.stream().map(WrapRequest::getParams).map(UserDO::getId).collect(Collectors.toList());
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
        // 用in语句合并成一条SQL，避免多次请求数据库的IO
        queryWrapper.in("id", userIds);
        List<UserDO> users = list(queryWrapper);
        Map<Long, List<UserDO>> userGroup = users.stream().collect(Collectors.groupingBy(UserDO::getId));
        HashMap<String, UserDO> result = new HashMap<>();
        for (WrapRequest<UserDO, UserDO> val : userReqs) {
            List<UserDO> usersList = userGroup.get(val.getParams().getId());
            if (!CollectionUtils.isEmpty(usersList)) {
                result.put(val.getRequestId(), usersList.get(0));
            } else {
                // 表示没数据
                result.put(val.getRequestId(), null);
            }
        }
        return result;
    }


}