package cn.piesat.tests.feign.consumer.controller;

import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;
import cn.piesat.tests.feign.consumer.model.entity.UserDO;
import cn.piesat.tests.feign.consumer.service.feign.UserFeignClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p/>
 *
 * @author zhouxp
 * @description :测试远程调用
 * <p/>
 * <b>@create:</b> 2022/10/9 10:49.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
@Tag(name="TestController",description = "测试管理")
public class TestController {
    private final UserFeignClient userFeignClient;

    @Operation(summary = "分页查询")
    @PostMapping("/list")
    public PageResult list(PageBean pageBean, @RequestBody(required = false) UserDO userDO){
        return userFeignClient.list(pageBean,userDO);
    }



    @Operation(summary = "根据id删除信息")
    @DeleteMapping("/delete")
    public Boolean delete(@RequestParam String id){
        System.out.println(id);
        return userFeignClient.delete(new ArrayList<Long>(){{add(1L);add(2L);}});
    }

    @Operation(summary = "所有记录")
    @PostMapping("/all")
    public List<UserDO> all(){
        List<UserDO> list =userFeignClient.all();
        return list;
    }

    @Operation(summary = "map")
    @PostMapping("/map")
    public Map<String,UserDO> map(){
        Map<String,UserDO> map =userFeignClient.map();
        return map;
    }
}
