package cn.piesat.tools.log.biz.controller;

import cn.piesat.framework.common.model.entity.OpLogEntity;
import cn.piesat.tools.log.biz.service.ClientLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * 执行日志
 *
 * @author zhouxp
 * @email zhouxiaoping@piesat.cn
 * @date 2023-09-04 17:13:20
 */
@Api(tags = "远程日志")
@RestController
@RequestMapping("/log")
@RequiredArgsConstructor
public class ClientLogController {

    private final ClientLogService clientLogService;

    @ApiOperation(value = "保存远程日志")
    @PostMapping("/add")
    public Boolean add(@RequestBody OpLogEntity opLogEntity){
        return clientLogService.add(opLogEntity);
    }

}
