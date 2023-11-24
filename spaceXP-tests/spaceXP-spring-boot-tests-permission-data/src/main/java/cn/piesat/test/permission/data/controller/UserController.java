package cn.piesat.test.permission.data.controller;


import cn.piesat.framework.common.annotation.NoApiResult;
import cn.piesat.framework.common.annotation.validator.group.AddGroup;
import cn.piesat.framework.common.annotation.validator.group.UpdateGroup;
import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;
import cn.piesat.framework.mybatis.plus.annotation.DynamicTableName;

import cn.piesat.test.permission.data.model.entity.UserDO;
import cn.piesat.test.permission.data.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

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
@NoApiResult
public class UserController {

    private final UserService userService;

    /**
     * 列表
     */
    @ApiOperation("分页查询")
    @PostMapping("/list")
    public PageResult list(PageBean pageBean, @RequestBody(required = false) UserDO userDO){
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
    @DeleteMapping("/delete")
    public Boolean delete(@RequestBody Long[] ids){
        return userService.delete(Arrays.asList(ids));
    }
    /**
     * 删除
     */
    @ApiOperation("根据id删除信息")
    @DeleteMapping("/delete/{id}")
    public Boolean delete(@PathVariable Long id){
        return userService.delete(id);
    }


    @ApiOperation("获取Person")
    @PostMapping("/getPerson")
    public Person getPerson( @RequestBody Person person){
        return person;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Person{
        private Integer age;
        private String name;
    }
    @ApiOperation("动态表名分页查询")
    @PostMapping("/list/{tableName}")
    @DynamicTableName
    public PageResult list(@PathVariable("tableName") String tableName, PageBean pageBean, @RequestBody(required = false) UserDO userDO){
        return userService.list(pageBean,userDO);

    }
}
