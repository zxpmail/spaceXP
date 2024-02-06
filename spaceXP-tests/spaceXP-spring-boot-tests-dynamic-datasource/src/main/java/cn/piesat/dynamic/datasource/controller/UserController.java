package cn.piesat.dynamic.datasource.controller;


import cn.piesat.dynamic.datasource.dao.mapper.UserMapper;
import cn.piesat.dynamic.datasource.service.impl.UserServiceTest;
import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;

import cn.piesat.dynamic.datasource.service.UserService;
import cn.piesat.dynamic.datasource.model.entity.UserDO;
import cn.piesat.framework.dynamic.datasource.annotation.DS;
import cn.piesat.framework.dynamic.datasource.model.DSEntity;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

/**
 * 用户信息
 *
 * @author zhouxp
 * @email zhouxiaoping@piesat.cn
 * @date 2023-01-16 08:52:49
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    @Qualifier("userService")
    @Resource
    private  UserService userService;

    /**
     * 列表
     */

    @PostMapping("/list")
    @DS
    public PageResult list(@RequestParam("pageBean") PageBean pageBean, @RequestBody(required = false) UserDO userDO, @RequestParam("dsEntity")DSEntity dsEntity){
        return userService.list(pageBean,userDO);

    }
    /**
     * 信息
     */

    @GetMapping("/info/{id}")
    public UserDO info(@PathVariable("id") Long id){
        return userService.info(id);
    }

    @Resource
    private UserMapper userMapper;

    @GetMapping("/{name}/list")
    public List<UserDO> list(@PathVariable("name")String name){
        if(name.equals("master")){
            return userMapper.queryAllWithMaster();
        }else{
            return userMapper.queryAllWithSlave();
        }
    }
    @Resource
    private UserServiceTest userServiceTest;

    @GetMapping("/com")
    public Object com(){
        return userServiceTest.get();
    }
}
