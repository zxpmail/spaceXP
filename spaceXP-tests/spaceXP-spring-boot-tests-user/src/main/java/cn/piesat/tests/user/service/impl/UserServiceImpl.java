package cn.piesat.tests.user.service.impl;


import cn.piesat.framework.common.constants.CommonConstants;
import cn.piesat.framework.common.model.dto.JwtUser;
import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;
import cn.piesat.framework.common.utils.JwtUtils;
import cn.piesat.framework.mybatis.plus.utils.QueryUtils;
import cn.piesat.framework.redis.core.RedisService;
import cn.piesat.tests.user.dao.mapper.UserMapper;
import cn.piesat.tests.user.model.entity.UserDO;
import cn.piesat.tests.user.properties.AdminProperties;
import cn.piesat.tests.user.service.UserService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service("userService")
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {
    private final RedisService redisService;

    public UserServiceImpl(RedisService redisService) {
        this.redisService = redisService;
    }

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
        return updateById(userDO);
    }

    @Override
    public Boolean delete(List<Long> asList) {
        return removeBatchByIds(asList);
    }

    @Override
    public Boolean delete(Long id) {
        return removeById(id);
    }

    @Resource
    private AdminProperties adminProperties;

    @Override
    public Map<String, Object> login(String userName, String password) {
        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserDO::getUsername, userName);
        List<UserDO> list = list(wrapper);
        if (CollectionUtils.isEmpty(list) && list.size() == 0) {
            throw new RuntimeException("没有用户");
        }
        UserDO userDO = list.get(0);

        JwtUser user = new JwtUser();
        user.setUserId(userDO.getId());
        user.setUserName(userDO.getUsername());
        user.setDeptName(userDO.getDeptId());
        user.setDeptId(Long.parseLong(userDO.getDeptId()));
        user.setTenantId(1L);
        String token = JwtUtils.createToken(user.getUserId().toString(), adminProperties.getTokenProperties().getTokenSignKey());
        log.info("获取用户权限并写入redis缓存中！");
        redisService.setObject(adminProperties.getTokenProperties().getLoginToken() + "_check_" + userDO.getId(), token);
        Map<String, Object> map = new HashMap<>(1);
        map.put(CommonConstants.TOKEN, token);
        redisService.setObject(adminProperties.getTokenProperties().getLoginToken() + user.getUserId(), JSON.toJSONString(user), adminProperties.getTokenProperties().getExpiration(), TimeUnit.SECONDS);
        return map;
    }

    @Override
    public Boolean logout(Long userId) {
        redisService.delete(adminProperties.getTokenProperties().getLoginToken() + "_check_" + userId);
        redisService.delete(adminProperties.getTokenProperties().getLoginToken() + userId);
        return true;
    }
}