package cn.piesat.test.file.controller;


import cn.piesat.framework.redis.core.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;


/**
 * 测试信息
 */
@Api(tags = "redis测试")
@RestController
@RequestMapping("redis")
@RequiredArgsConstructor
public class RedisController {

    private final RedisService redisService;

    /**
     * 信息
     */
    @ApiOperation("根据key查询")
    @GetMapping("{key}")
    public Object info(@PathVariable("key") String key){
        return redisService.getObject(key);
    }

    /**
     * 信息
     */
    @ApiOperation("设置")
    @GetMapping("{key}/{value}")
    public Object set(@PathVariable("key") String key,@PathVariable("value") String value){
         redisService.setObject(key,value);
         return "ok";
    }
    /**
     * 信息
     */
    @ApiOperation("设置hash")
    @GetMapping("setHash/{key}")
    public Object set(@PathVariable("key") String key){
        ArrayList<String> list = new ArrayList<>();
        list.add("aa");
        list.add("bb");
        redisService.setMapValue("hello",key,list);
        return "ok";
    }

    @ApiOperation("设置hash")
    @GetMapping("getHash/{key}")
    public Object get(@PathVariable("key") String key){
         return redisService.getMapValue("hello", key);
    }

    @ApiOperation("设置hash")
    @GetMapping("deleteHash/{key}")
    public void deleteHash(@PathVariable("key") String key){
         redisService.deleteMapMatching("hello", key);
    }
}
