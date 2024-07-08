package cn.piesat.tools.log.biz.controller;


import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;

import cn.piesat.tools.log.api.model.query.LogQuery;
import cn.piesat.tools.log.api.model.vo.LogVO;
import cn.piesat.tools.log.biz.service.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 执行日志
 *
 * @author zhouxp
 * @email zhouxiaoping@piesat.cn
 * @date 2023-09-04 17:13:20
 */
@Api(tags = "执行日志")
@RestController
@RequestMapping("/log")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @ApiOperation("分页查询")
    @PostMapping("/list")
    public PageResult list(PageBean pageBean, @RequestBody(required = false) LogQuery logQuery){
        return logService.list(pageBean,logQuery);
    }

    @ApiOperation("根据id查询")
    @GetMapping("/info/{id}")
    public LogVO info(@PathVariable("id") Long id){
        return logService.info(id);
    }

    @ApiOperation("按时间删除信息")
    @DeleteMapping("/delete/{startTime}/{endTime}")
    public Boolean delete(@PathVariable("startTime")@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime, @PathVariable("endTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime){
        return logService.delete(startTime,endTime);
    }

}
