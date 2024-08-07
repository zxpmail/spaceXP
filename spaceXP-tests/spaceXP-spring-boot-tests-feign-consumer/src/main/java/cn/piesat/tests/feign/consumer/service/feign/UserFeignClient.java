package cn.piesat.tests.feign.consumer.service.feign;


import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;
import cn.piesat.framework.feign.annotation.HasApiResult;
import cn.piesat.framework.feign.core.FeignRequestInterceptor;
import cn.piesat.tests.feign.consumer.model.entity.UserDO;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * <p/>
 * @author zhouxp
 * {@code @description} :RPC调用部门信息
 * <p/>
 * <b>@create:</b> 2022/10/9 10:33.
 */
@HasApiResult
@FeignClient(name = "producer" ,path ="producer", configuration = FeignRequestInterceptor.class)
@Hidden
public interface UserFeignClient {
    @ApiOperation("分页查询")
    @PostMapping("/user/list")
    PageResult list(@RequestParam(value = "pageBean") PageBean pageBean, @RequestBody UserDO userDO);


    @PostMapping("/user/delete")
    Boolean delete(@RequestBody List<Long> ids);

    @GetMapping("/user/all")
    List<UserDO> all();

    @PostMapping ("/user/map")
    Map<String,UserDO> map();


    @GetMapping ("/user/null")
    List<UserDO> getNull();
}
