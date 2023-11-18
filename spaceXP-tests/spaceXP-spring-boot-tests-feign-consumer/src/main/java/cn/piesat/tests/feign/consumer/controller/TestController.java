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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

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
    @NoApiResult
    public PageResult list(PageBean pageBean, @RequestBody(required = false) UserDO userDO){
        return userFeignClient.list(pageBean,userDO);
    }

    @PostMapping(value = "/testDate",produces = MediaType.APPLICATION_JSON_VALUE)
    @NoApiResult
   // @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime testDate(){
        return userFeignClient.testDate();
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private final static class Person {
        private String name;
        private int age;
    }

    public static void main(String[] args) throws JsonProcessingException {
        String receivedJsonString = "{\"name\":\"Alice\",\"age\":30,\"city\":\"New York\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        Person receivedPerson = objectMapper.readValue(receivedJsonString, Person.class);
        System.out.println(receivedPerson);
    }
}
