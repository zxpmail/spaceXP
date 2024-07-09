package cn.piesat.framework.log.external.service;


import cn.piesat.framework.common.model.entity.OpLogEntity;
import cn.piesat.framework.common.model.vo.ApiResult;
import cn.piesat.framework.log.external.properties.ToolsLogProperties;
import cn.piesat.framework.log.service.ExecuteLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

/**
 * <p/>
 * {@code @description}  :执行日志服务
 * <p/>
 * <b>@create:</b> 2023/9/4 15:49.
 *
 * @author zhouxp
 */

@RequiredArgsConstructor
public class LogExecuteService implements ExecuteLogService {

    private final ToolsLogProperties toolsLogProperties;
    private final RestTemplate restTemplate;

    @Autowired(required = false)
    private LogFeignClient logFeignClient;
    @Override
    public void exec(OpLogEntity opLogEntity) {
        logFeignClient.add(opLogEntity);
        //restTemplate.postForEntity(toolsLogProperties.getRestUrlPrefix() +"/log/log/add",opLogEntity, ApiResult.class);
    }
}
