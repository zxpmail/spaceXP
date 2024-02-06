package cn.piesat.permission.data.controller;

import cn.piesat.framework.common.annotation.validator.group.AddGroup;
import cn.piesat.framework.common.annotation.validator.group.UpdateGroup;
import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;

import cn.piesat.permission.data.service.UserService;
import cn.piesat.permission.data.model.entity.UserDO;

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
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 列表
     */
    @PostMapping("/list")
    public PageResult list(PageBean pageBean, @RequestBody(required = false) UserDO userDO){
        return userService.list(pageBean,userDO);

    }
    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public UserDO info(@PathVariable("id") Long id){
        return userService.info(id);
    }
    /**
     * 保存
     */
    @PostMapping("/save")
    public Boolean save(@Validated(AddGroup.class) @RequestBody UserDO userDO){
        return userService.add(userDO);
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Boolean update(@Validated(UpdateGroup.class) @RequestBody UserDO userDO){
        return userService.update(userDO);
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public Boolean delete(@RequestBody Long[] ids){
        return userService.delete(Arrays.asList(ids));
    }
    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Boolean delete(@PathVariable Long id){
        return userService.delete(id);
    }

}
