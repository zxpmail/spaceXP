package cn.piesat.test.file.controller;


import cn.piesat.framework.common.annotation.LoginUser;
import cn.piesat.framework.common.annotation.NoApiResult;
import cn.piesat.framework.common.model.dto.JwtUser;
import cn.piesat.framework.common.utils.ValidationUtils;
import cn.piesat.framework.web.annotation.ConditionalValidate;
import cn.piesat.test.file.model.entity.Student;
import cn.piesat.test.file.model.entity.TestValidate;
import cn.piesat.test.file.service.TestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * {@code @description}  :测试
 * <p/>
 * <b>@create:</b> 2023/1/10 10:06.
 *
 * @author zhouxp
 */
@RestController
@Api(tags = "测试信息")
@RequestMapping("/test")
@Slf4j
public class TestController {
    /**
     * 测试根据配置文件修改枚举
     */
    @PostMapping(value = "/testString")
    private String testString(@RequestBody String testString) {
        return testString;
    }
    @ApiOperation("测试根据配置文件修改枚举")
    @GetMapping("/changeEnum")
    private String changeEnum() {
        return "ok";
    }


    @GetMapping("get")
    @NoApiResult
    public String test() {
        return "hello";
    }

    @GetMapping("get1")
    public String test1() {
        throw new RuntimeException("hh");
    }

    @GetMapping("get2")
    public JwtUser test2(@LoginUser JwtUser jwtUser) {
        return jwtUser;
    }

    @GetMapping("assert")
    public void testAssert() {
        Assert.notNull(null, "Value must not be null");
    }

    private static int count = 0;

    @GetMapping("hello")
    public String hello() throws InterruptedException {
        count++;
        if (count % 10 == 9) {
            Thread.sleep(100000000);
        }
        return "Hello, World!";
    }

    @ApiOperation(value = "导入")
    @PostMapping("/import")
    public void importFile(@RequestPart MultipartFile file) {
    }

    @GetMapping("map")
    public Map<String, Long> map() {
        return new HashMap<String, Long>() {{
            put("k1", 1L);
            put("k2", 2L);
        }};
    }

    //实体
    @PostMapping("modelXssFilter")
    public UserEntity modelXssFilter(@RequestBody UserEntity user) {
        log.error(user.getUsername() + "---" + user.getMobile());
        return user;
    }

    /**
     *
     <a href="http://localhost:8080/test/testXss?param=1">...</a>
     http://localhost:8080/test/testXss?param="参数"
     http://localhost:8080/test/testXss?param=<aside><a href="#" target="_blank">链接</a></aside>
     http://localhost:8080/test/testXss?param=<script>alert("1")</script>
     */
    //不转义
    @GetMapping("/testXss")
    public void openXssFilter(String param) {
        log.error(param);
    }

    @Data
    static
    class UserEntity {
        private Long id;
        private String username;
        private String mobile;
        private LocalDateTime createTime;
    }

    @PostMapping(value = "/upload")
    public void upload(@RequestPart @RequestParam(value = "file" ) MultipartFile file)  {
        log.error("log 日志1");
        log.info("log 日志2 ");
    }

    @GetMapping ("/long2string")
    public Object long2string()  {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1234567891234567891L);
        userEntity.setMobile("1235");
        userEntity.setUsername("zxp");
        userEntity.setCreateTime(LocalDateTime.now());
        HashMap<String, Object> map =new HashMap<>();
        map.put("id",userEntity);
        return map;
    }

    @GetMapping ("/object")
    public Object getObject()  {
        return new Integer(0);
    }

    @Resource
    private TestService testService;
    @PostMapping("getStu")
    public Student getStu(@RequestBody   Student stu){
        return testService.
                testStu(stu,null);
    }

    @GetMapping("testValid")
    public Student testValid(){
        Student student = new Student();
        student.setId(1);
        ValidationUtils.validate(student);
        return student;
    }

    @Resource
    private RestTemplate restTemplate;
    @GetMapping("testC")
    public FStat testC(){

        TStat encrypt = new TStat("40", "ICgYFgQWD8AAAARlCgEKAaqqqqqqNPU=", "encrypt", "1831992120334823425");
        FStat fStat = restTemplate.postForObject("http://111.203.213.54:18887", encrypt, FStat.class);
        return fStat;
    }
    @Data
    @AllArgsConstructor
    static class TStat{
      String satID;
      String srcFrame;
      String reqType;
      String reqID;
    }

    @Data
    static class FStat{
       String encFrame;
       String reqID;
       String result;
       String satID;
    }

    @PostMapping("/testValidate")
    @ConditionalValidate
    public String testValidate(@RequestBody @Validated TestValidate testValidate) {
        System.out.println(testValidate);
        return "success";
    }
}
