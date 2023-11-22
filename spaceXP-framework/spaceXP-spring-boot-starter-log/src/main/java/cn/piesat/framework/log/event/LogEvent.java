package cn.piesat.framework.log.event;


import cn.piesat.framework.common.model.entity.OpLogEntity;
import org.springframework.context.ApplicationEvent;

/**
 * <p/>
 * {@code @description}  :系统日志事件
 * <p/>
 * <b>@create:</b> 2022/11/28 9:35.
 *
 * @author zhouxp
 */
public class LogEvent extends ApplicationEvent {
    public LogEvent(OpLogEntity opLogEntity) {
        super(opLogEntity);
    }
}
