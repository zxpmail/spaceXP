package cn.piesat.kafka.datasource.controller;

import cn.piesat.kafka.datasource.model.TestDTO;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p/>
 * {@code @description}  : 生产端测试
 * <p/>
 * <b>@create:</b> 2024-03-21 15:13.
 *
 * @author zhouxp
 */
@RestController
@RequestMapping(value = "/test")
@Slf4j
@DependsOn("kafkaCreator") //必须，不然出错
public class TestController {

    @Resource //或者@Autowired和    @Qualifier("producer10")
    private KafkaTemplate<String,String> producer10;

    @Resource
    private KafkaTemplate<String,String>  producer20;


    @PostMapping("/test10")
    public Object test10(@RequestBody TestDTO testDTO) {
        producer10.send("test10", JSON.toJSONString(testDTO));
        return testDTO;
    }
    @PostMapping("/test11")
    public Object test11(@RequestBody TestDTO testDTO) {
        producer10.send("test11", JSON.toJSONString(testDTO));
        return testDTO;
    }
    @PostMapping("/test20")
    public Object test20(@RequestBody TestDTO testDTO) {
        producer20.send("test20", JSON.toJSONString(testDTO));
        return testDTO;
    }
}

