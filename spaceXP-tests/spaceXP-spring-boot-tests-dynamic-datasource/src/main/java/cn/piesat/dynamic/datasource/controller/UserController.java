package cn.piesat.dynamic.datasource.controller;


import cn.piesat.dynamic.datasource.dao.mapper.UserMapper;
import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;

import cn.piesat.dynamic.datasource.service.UserService;
import cn.piesat.dynamic.datasource.model.entity.UserDO;
import cn.piesat.framework.dynamic.datasource.annotation.DS;
import cn.piesat.framework.dynamic.datasource.model.DSEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户信息
 *
 * @author zhouxp
 * @email zhouxiaoping@piesat.cn
 * @date 2023-01-16 08:52:49
 */
@Api(tags = "用户信息")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 列表
     */
    @ApiOperation("分页查询")
    @PostMapping("/list")
    @DS
    public PageResult list(PageBean pageBean, @RequestBody(required = false) UserDO userDO, DSEntity dsEntity){
        return userService.list(pageBean,userDO);

    }
    /**
     * 信息
     */
    @ApiOperation("根据id查询")
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


    @GetMapping("/add1")
    public String add1(){
        userService.addTest1();
        return "add1";
    }

    @GetMapping("/add2")
    public String add2(){
        userService.addTest2();
        return "add2";
    }

    @GetMapping("/addThread")
    public String addThread(){
        new Thread(userService::addTest2).start();
        new Thread(userService::addTest1).start();
        return "add2";
    }

    @GetMapping("/execXa")
    public String execXa(){
        userService.execXa();
        return "execXa";
    }
}
