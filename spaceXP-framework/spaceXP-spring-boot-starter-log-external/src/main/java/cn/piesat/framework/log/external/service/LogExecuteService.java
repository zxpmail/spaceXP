package cn.piesat.framework.log.external.service;


import cn.piesat.framework.common.model.entity.OpLogEntity;
import cn.piesat.framework.common.model.vo.ApiResult;
import cn.piesat.framework.log.external.properties.LogExternalProperties;
import cn.piesat.framework.log.external.client.LogFeignClient;
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

    private final LogExternalProperties logExternalProperties;
    @Autowired(required = false)
    private  RestTemplate restTemplate;

    @Autowired(required = false)
    private LogFeignClient logFeignClient;
    @Override
    public void exec(OpLogEntity opLogEntity) {
        if(logExternalProperties.getRestTemplateEnabled()){
            restTemplate.postForEntity(logExternalProperties.getRestUrlPrefix() + logExternalProperties.getSave(),opLogEntity, ApiResult.class);
        }else{
            logFeignClient.add(opLogEntity);
        }
    }
}
