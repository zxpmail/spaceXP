package cn.piesat.tests.feign.consumer.controller;


import cn.piesat.framework.common.annotation.NoApiResult;
import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;
import cn.piesat.tests.feign.consumer.model.entity.UserDO;
import cn.piesat.tests.feign.consumer.service.feign.UserFeignClient;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
public class TestController {
    private final UserFeignClient userFeignClient;

    @ApiOperation("分页查询")
    @PostMapping("/list")
    public PageResult list(PageBean pageBean, @RequestBody(required = false) UserDO userDO){
        return userFeignClient.list(pageBean,userDO);
    }



    @ApiOperation("根据id删除信息")
    @DeleteMapping("/delete")
    public Boolean delete(@RequestParam String id){
        System.out.println(id);
        return userFeignClient.delete(new ArrayList<Long>(){{add(1L);add(2L);}});
    }

    @ApiOperation("所有记录")
    @PostMapping("/all")
    public List<UserDO> all(){
        return userFeignClient.all();
    }
}
