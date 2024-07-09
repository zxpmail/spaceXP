package cn.piesat.tests.log.controller;



import cn.piesat.framework.log.annotation.OpLog;
import cn.piesat.framework.common.model.enums.BusinessEnum;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;


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
    public Object log()  {
        return "ok";
    }
}
