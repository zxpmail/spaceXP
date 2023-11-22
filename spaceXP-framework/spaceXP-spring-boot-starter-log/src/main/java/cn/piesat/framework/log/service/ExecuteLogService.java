package cn.piesat.framework.log.service;


import cn.piesat.framework.common.model.entity.OpLogEntity;

/**
 * <p/>
 * {@code @description}  :日志服务类
 * <p/>
 * <b>@create:</b> 2022/11/28 9:39.
 *
 * @author zhouxp
 */
public interface ExecuteLogService {
    void exec(OpLogEntity opLogEntity);
}
