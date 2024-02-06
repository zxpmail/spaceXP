package cn.piesat.tests.redis.controller;


import cn.piesat.framework.redis.annotation.AccessLimit;
import cn.piesat.framework.redis.annotation.PreventReplay;
import cn.piesat.framework.redis.core.RedisService;
import cn.piesat.framework.redis.model.MessageBody;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * 测试信息
 */
@RestController
@RequiredArgsConstructor
public class TestController {

    private final RedisService redisService;


    @PostMapping("/message")
    public void sendMessage(@RequestParam String message) {
        // 发布消息
        MessageBody messageBody = new MessageBody();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        messageBody.setData(timeFormatter.format(now));
        messageBody.setTitle("日常信息");
        messageBody.setContent("hello world!");
        redisService.convertAndSend("TOPIC",messageBody);
    }
    /**
     * 信息
     */
    @GetMapping("{key}")
    public Object info(@PathVariable("key") String key){
        return redisService.getObject(key);
    }

    /**
     * 信息
     */

    @GetMapping("{key}/{value}")
    public Object set(@PathVariable("key") String key,@PathVariable("value") String value){
         redisService.setObject(key,value);
         return "ok";
    }
    /**
     * 信息
     */

    @GetMapping("setHash/{key}")
    public Object set(@PathVariable("key") String key){
        ArrayList<String> list = new ArrayList<>();
        list.add("aa");
        list.add("bb");
        redisService.setMapValue("hello",key,list);
        return "ok";
    }


    @GetMapping("getHash/{key}")
    public Object get(@PathVariable("key") String key){
         return redisService.getMapValue("hello", key);
    }


    @GetMapping("deleteHash/{key}")
    public void deleteHash(@PathVariable("key") String key){
         redisService.deleteMapMatching("hello", key);
    }


    @GetMapping("preventReplay/{key}")
    @PreventReplay(value = 10)
    public Object  preventReplay(@PathVariable("key") String key){
        return new HashMap<String, Object>(){{put(key,"hello");}};
    }


    @GetMapping("accessLimit/{key}")
    @AccessLimit(maxCount = 4)
    public Object  accessLimit(@PathVariable("key") String key){
        return new HashMap<String, Object>(){{put(key,"hello");}};
    }
}
