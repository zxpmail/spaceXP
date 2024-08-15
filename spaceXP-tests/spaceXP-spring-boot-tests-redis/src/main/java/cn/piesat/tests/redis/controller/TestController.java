package cn.piesat.tests.redis.controller;


import cn.piesat.framework.redis.annotation.AccessLimit;
import cn.piesat.framework.redis.annotation.PreventReplay;
import cn.piesat.framework.redis.core.RedisService;
import cn.piesat.framework.redis.external.annotation.DLock;
import cn.piesat.framework.redis.model.MessageBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 测试信息
 */
@Api(tags = "测试")
@RestController
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final RedisService redisService;


    @PostMapping("/message")
    public void sendMessage(@RequestParam String message) {
        // 发布消息
        MessageBody<Integer> messageBody = new MessageBody<>();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        messageBody.setData(message +","+ timeFormatter.format(now));
        messageBody.setToId(1);
        messageBody.setFromId(2);
        messageBody.setType(0);
        redisService.convertAndSend("TOPIC",messageBody);
    }
    /**
     * 信息
     */
    @ApiOperation("根据key查询")
    @GetMapping("/info/{key}")
    public Object info(@PathVariable("key") String key){
        return redisService.getObject(key);
    }

    /**
     * 信息
     */
    @ApiOperation("设置")
    @GetMapping("set/{key}/{value}")
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

    @ApiOperation("获取hash")
    @GetMapping("getHash/{key}")
    public Object get(@PathVariable("key") String key){
         return redisService.getMapValue("hello", key);
    }

    @ApiOperation("获取allHash")
    @GetMapping("getAllHash")
    public Map<String, List<String>> getAllHash(){
        return redisService.getMap("hello");
    }

    @ApiOperation("删除hash")
    @GetMapping("deleteHash/{key}")
    public void deleteHash(@PathVariable("key") String key){
         redisService.deleteMapMatching("hello", key);
    }

    @ApiOperation("测试防止重刷")
    @GetMapping("preventReplay/{key}")
    @PreventReplay(value = 10)
    public Object  preventReplay(@PathVariable("key") String key){
        return new HashMap<String, Object>(){{put(key,"hello");}};
    }

    @ApiOperation("限流")
    @GetMapping("accessLimit/{key}")
    @AccessLimit(maxCount = 4)
    public Object  accessLimit(@PathVariable("key") String key){
        return new HashMap<String, Object>(){{put(key,"hello");}};
    }
    @ApiOperation("限流1")
    @GetMapping("accessLimit1")
    @AccessLimit(maxCount = 4)
    public Object  accessLimit1(){
        return new HashMap<String, Object>(){{put("1","hello");}};
    }

    @ApiOperation("限流2")
    @PostMapping("accessLimit2")
    @AccessLimit(maxCount = 4)
    public Object  accessLimit2(@RequestPart MultipartFile file){
        return new HashMap<String, Object>(){{put("2","hello");}};
    }
    @GetMapping("testLock")
    @DLock("'person:' + #person.id + ':' + #cateId")
    public void testLock( Person person,Integer cateId) throws InterruptedException {
        System.out.printf("当前执行线程: %s%n", Thread.currentThread().getName()) ;
        log.info("id: {} , cateId:{}",person.getId(),cateId);
        Thread.sleep(20000);
    }

    @Data
    static  class Person{
        Integer id;
    }
    @Resource
    private IdGeneratorService idGeneratorService;

    @GetMapping("generateId")
    void generateIdTest() {
        Long code = idGeneratorService.generateId("orderId", 9);
        System.out.println(code);
    }

    @Resource
    private TestService testService;
    @GetMapping("testLock1")
    public void testLock()  {
        testService.testLock();

    }
}
