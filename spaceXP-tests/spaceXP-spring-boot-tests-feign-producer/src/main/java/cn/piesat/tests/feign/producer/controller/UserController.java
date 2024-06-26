package cn.piesat.tests.feign.producer.controller;


import cn.piesat.framework.common.annotation.validator.group.AddGroup;
import cn.piesat.framework.common.annotation.validator.group.UpdateGroup;
import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;
import cn.piesat.tests.feign.producer.model.entity.UserDO;
import cn.piesat.tests.feign.producer.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author zhouxp
 * @email zhouxiaoping@piesat.cn
 * @date 2022-10-08 14:43:40
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @ApiOperation("map")
    @PostMapping("/map")
    public Map<String,UserDO> map(){
        Map<String,UserDO> map =new HashMap<>();
        UserDO userDO =new UserDO();
        userDO.setId(1L);
        userDO.setNickname("hello");
        map.put("key1",userDO);
        map.put("key2",userDO);
        return map;
    }
    /**
     * 列表
     */
    @ApiOperation("分页查询")
    @PostMapping("/list")
    public PageResult list(@RequestParam(required = false) String parentaskid, PageBean pageBean, @RequestBody(required = false) UserDO userDO){
       // throw new RuntimeException("test");
        return userService.list(pageBean,userDO);

    }

    /**
     * 列表
     */
    @ApiOperation("所有记录")
    @GetMapping("/all")
    public List<UserDO> all(){
        return  userService.list();

    }
    /**
     * 信息
     */
    @ApiOperation("根据id查询")
    @GetMapping("/info/{id}")
    public UserDO info(@PathVariable("id") Long id){
        return userService.info(id);
    }
    /**
     * 保存
     */
    @ApiOperation("保存信息")
    @PostMapping("/save")
    public Boolean save(@Validated(AddGroup.class) @RequestBody UserDO userDO){
        return userService.add(userDO);
    }

    /**
     * 修改
     */
    @ApiOperation("修改信息")
    @PutMapping("/update")
    public Boolean update(@Validated(UpdateGroup.class) @RequestBody UserDO userDO){
        return userService.update(userDO);
    }

    /**
     * 删除
     */
    @ApiOperation("批量删除信息")
    @PostMapping("/delete")
    public Boolean delete(@RequestBody List<Long> ids){
        return userService.delete(ids);
    }
    /**
     * 删除
     */
    @ApiOperation("根据id删除信息")
    @DeleteMapping("/delete/{id}")
    public Boolean delete(@PathVariable Long id){
        return userService.delete(id);
    }

}
