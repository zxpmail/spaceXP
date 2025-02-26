package cn.piesat.tests.mybaits.plus.controller;


import cn.piesat.framework.common.annotation.validator.group.AddGroup;
import cn.piesat.framework.common.annotation.validator.group.UpdateGroup;
import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;
import cn.piesat.framework.mybatis.plus.model.TableNameEntity;
import cn.piesat.tests.mybaits.plus.model.entity.UserDO;
import cn.piesat.tests.mybaits.plus.service.UserService;
import cn.piesat.tests.mybaits.plus.service.impl.UserByIdBatchQueryQueueService;
import cn.piesat.tests.mybaits.plus.service.impl.UserByUserBatchQueryQueueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.Callable;

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
    /**
     * 列表
     */
    @ApiOperation("分页查询")
    @PostMapping("/list")
    public PageResult list(@RequestParam(required = false) String parentaskid, PageBean pageBean, @RequestBody(required = false) UserDO userDO){
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


    @ApiOperation("根据id动态贴换表名查询")
    @GetMapping("/dynamicInfo/{id}")
    public UserDO dynamicInfo( TableNameEntity tableName, @PathVariable("id") Long id){
        return userService.dynamicInfo(tableName,id);
    }

    @ApiOperation("查询用户信息")
    @GetMapping("/getUserByName")
    public UserDO getUserByName(String name){
        return userService.getUserByName(name);
    }


    @Setter(onMethod_ = @Autowired)
    private UserByIdBatchQueryQueueService userBatchQueryQueueService;

    @GetMapping("/merge")
    public Callable<UserDO> merge(Long userId) {
        return () -> userBatchQueryQueueService.getByIdUser(userId);
    }

    @Setter(onMethod_ = @Autowired)
    private UserByUserBatchQueryQueueService userByUserBatchQueryQueueService;
    @PostMapping("/postMerge")
    public Callable<UserDO> postMerge(@RequestBody  UserDO userDO ) {
        return () -> userByUserBatchQueryQueueService.getByIdUser(userDO);
    }

}
