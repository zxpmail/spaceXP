package cn.piesat.tests.log.controller;


import cn.piesat.framework.common.exception.BaseException;
import cn.piesat.framework.common.model.enums.CommonResponseEnum;
import cn.piesat.framework.log.annotation.MdcLog;
import cn.piesat.framework.log.annotation.OpLog;
import cn.piesat.framework.common.model.enums.BusinessEnum;
import cn.piesat.framework.log.enums.MdcLogType;
import cn.piesat.framework.log.external.DynamicLoggingConfigurer;
import cn.piesat.tests.log.service.TestService;
import cn.piesat.tests.log.service.TestService1;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p/>
 * {@code @description}  :测试
 * <p/>
 * <b>@create:</b> 2022/12/9 17:22.
 *
 * @author zhouxp
 */
@RestController
@Slf4j
public class TestController {
    @GetMapping(value = "/log")
    @OpLog(op = BusinessEnum.CLEAN,description = "测试")
    @Async
    public void log()  {
        log.error("log 日志1");
        log.info("log 日志2 ");
        throw new BaseException(CommonResponseEnum.ERROR);
    }
    @Resource
    private TestService1 testService1;
    @GetMapping(value = "/mdcLogBiz")
    @MdcLog(op= MdcLogType.BIZ,appName = "Test")
    public String mdcLogBiz(String name,Integer age)  {

        log.error("log 日志1");
        log.info("log 日志2 ");
        testService1.testMdc();
        return "hello world!!";
    }
    @GetMapping(value = "/mdcLogApp")
    @MdcLog(op= MdcLogType.APP,appName = "Test")
    public String mdcLoApp()  {
        log.error("log 日志1");
        log.info("log 日志2 ");
        testService1.hello();
        return "hello world!!";
    }
    @GetMapping(value = "/mdcLogAudit")
    @MdcLog(op= MdcLogType.AUDIT,appName = "Test")
    public String mdcLoAudit()  {
        log.error("log 日志1");
        log.info("log 日志2 ");
        testService1.hello1();
        return "hello world!!";
    }
    @GetMapping(value = "/mdcLogException")
    @MdcLog(op= MdcLogType.BIZ,appName = "Test")
    public String mdcLogException()  {

        log.error("log 日志1");
        log.info("log 日志2 ");
        throw  new BaseException("hello");
    }
    @GetMapping(value = "/logSwagger")
    @ApiOperation("测试swagger拦截日志")
    public void logSwagger()  {
        log.error("log 日志1");
        log.info("log 日志2 ");
        throw new BaseException(CommonResponseEnum.ERROR);
    }

    @OpLog(op = BusinessEnum.CLEAN,description = "批量文件上传")
    @PostMapping(value = "/multiFileUpload")
    @ApiImplicitParam(name = "file", value = "上传的文件", dataType = "java.io.File", required = true)
    public void multiFileUpload(@RequestPart("file" ) MultipartFile[] file)  {
        log.error("log 日志1");
        log.info("log 日志2 ");
        throw new BaseException(CommonResponseEnum.ERROR);
    }

    @OpLog(op = BusinessEnum.CLEAN,description = "文件上传")
    @PostMapping(value = "/upload")
    public void upload(@RequestPart @RequestParam(value = "file" ) MultipartFile file)  {
        log.error("log 日志1");
        log.info("log 日志2 ");
        throw new BaseException(CommonResponseEnum.ERROR);
    }

    @OpLog(op = BusinessEnum.CLEAN,description = "测试")
    @GetMapping(value = "/testParam")
    public void testParam(HttpServletRequest req, String name, HttpServletResponse response)  {
        log.debug(".....debug....");
        log.info(".....info.....");
        log.warn("........warn.......");
        log.error("........error.......");
    }
    @Resource
    private TestService testService;
    @PostMapping(value = "/testService")
    public void testService()  {
       testService.testService();
    }

    @GetMapping(value = "/writeLog")
    public void writeLog()  {
        log.debug(".....debug....");
        log.info(".....info.....");
        log.warn("........warn.......");
        log.error("........error.......");
    }

    @Resource
    private DynamicLoggingConfigurer  loggingConfigurer;
    @GetMapping("/setLogLevel")
    public String setLogLevel(@RequestParam String packageName, @RequestParam String level) {
        loggingConfigurer.setLogLevel(packageName, level);
        return "Log level for " + packageName + " set to " + level;
    }

    @GetMapping("/resetLogLevel")
    public String resetLogLevel(@RequestParam String packageName) {
        loggingConfigurer.resetLogLevel(packageName);
        return "Log level for " + packageName + " reset to default";
    }
}
