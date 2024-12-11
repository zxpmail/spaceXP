package cn.piesat.tests.gray.service.impl;
import cn.piesat.framework.common.constants.CommonConstants;
import cn.piesat.framework.common.model.dto.JwtUser;
import cn.piesat.framework.common.utils.JwtUtils;
import cn.piesat.framework.redis.core.RedisService;
import cn.piesat.tests.gray.dao.mapper.UserMapper;
import cn.piesat.tests.gray.model.entity.UserDO;
import cn.piesat.tests.gray.properties.AdminProperties;


import cn.piesat.tests.gray.service.UserService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
}