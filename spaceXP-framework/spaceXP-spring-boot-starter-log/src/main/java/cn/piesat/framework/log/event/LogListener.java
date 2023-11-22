package cn.piesat.framework.log.event;


import cn.piesat.framework.common.model.entity.OpLogEntity;
import cn.piesat.framework.log.service.ExecuteLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.Resource;

/**
 * <p/>
 * {@code @description}  :注解形式的监听 异步监听日志事件
 * <p/>
 * <b>@create:</b> 2022/11/28 9:37.
 *
 * @author zhouxp
 */
@Slf4j
public class LogListener {

    @Resource
    private ExecuteLogService executeLogService;

    @Async("asyncExecutor")
    @EventListener(LogEvent.class)
    public void saveLog(LogEvent event) {
        OpLogEntity logDO = (OpLogEntity) event.getSource();
        // 保存日志
        executeLogService.exec(logDO);
    }
}
