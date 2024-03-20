package cn.piesat.tests.log.controller;


import cn.piesat.framework.common.exception.BaseException;
import cn.piesat.framework.common.model.enums.CommonResponseEnum;
import cn.piesat.framework.log.annotation.OpLog;
import cn.piesat.framework.common.model.enums.BusinessEnum;
import cn.piesat.tests.log.service.TestService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
    public void log() throws InterruptedException {
        log.error("log 日志1");
        log.info("log 日志2 ");
        throw new BaseException(CommonResponseEnum.ERROR);
    }

    @GetMapping(value = "/logSwagger")
    @ApiOperation("测试swagger拦截日志")
    public void logSwagger()  {
        log.error("log 日志1");
        log.info("log 日志2 ");
        throw new BaseException(CommonResponseEnum.ERROR);
    }

    @OpLog(op = BusinessEnum.CLEAN,description = "测试")
    @PostMapping(value = "/upload")
    public void upload(@RequestPart @RequestParam(value = "file" ) MultipartFile[] file)  {
        log.error("log 日志1");
        log.info("log 日志2 ");
        throw new BaseException(CommonResponseEnum.ERROR);
    }

    @OpLog(op = BusinessEnum.CLEAN,description = "测试")
    @PostMapping(value = "/multiFileUpload")
    public void upload(HttpServletRequest request)  {
        log.error("log 日志1");
        log.info("log 日志2 ");
        throw new BaseException(CommonResponseEnum.ERROR);
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
}
