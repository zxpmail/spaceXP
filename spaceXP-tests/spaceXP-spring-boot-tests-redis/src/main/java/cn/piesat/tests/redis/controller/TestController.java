package cn.piesat.tests.redis.controller;


import cn.piesat.framework.common.model.entity.MessageEntity;
import cn.piesat.framework.redis.annotation.AccessLimit;
import cn.piesat.framework.redis.annotation.PreventReplay;
import cn.piesat.framework.redis.bean.RedisQueueMessage;
import cn.piesat.framework.redis.core.DelayingQueueService;
import cn.piesat.framework.redis.core.RedisService;
import cn.piesat.framework.redis.external.annotation.DLock;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;


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
    public void sendMessage(@RequestBody MessageEntity messageEntity) {
        // 发布消息
        redisService.convertAndSend("TOPIC",messageEntity);
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

    private static final String beanName = "sampleRedisQueueHandleService";

    @Resource
    private DelayingQueueService delayingQueueService;

    /**
     * 发送消息
     *
     * @param msg
     */
    @GetMapping("/sendDelayingMessage")
    public String sendDelayingMessage(String msg, long delay) {
        try {
            if (msg != null) {
                String seqId = UUID.randomUUID().toString();
                RedisQueueMessage redisQueueMessage = new RedisQueueMessage();
                //时间戳默认为毫秒 延迟5s即为 5*1000
                long time = System.currentTimeMillis();
                LocalDateTime dateTime = Instant.ofEpochMilli(time).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
                redisQueueMessage.setDelayTime(time +  (delay * 1000));
                redisQueueMessage.setCreateTime(dateTime);
                redisQueueMessage.setBody(msg);
                redisQueueMessage.setId(seqId);
                redisQueueMessage.setBeanName(beanName);
                delayingQueueService.push(redisQueueMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }
}
