package cn.piesat.framework.log.external.client;

import cn.piesat.framework.common.model.entity.OpLogEntity;
import cn.piesat.framework.feign.annotation.HasApiResult;
import cn.piesat.framework.feign.core.FeignRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p/>
 * {@code @description}:
 * <p/>
 * {@code @create}: 2024-07-09 11:03
 * {@code @author}: zhouxp
 */
@HasApiResult
@FeignClient(value = "${space.log.external.logServerName:log}" ,url="${space.log.external.restUrlPrefix:''}", configuration = FeignRequestInterceptor.class)
public interface LogFeignClient {
    @PostMapping("${space.log.external.save:/log/add}")
    Boolean add(@RequestBody OpLogEntity opLogEntity);

}
