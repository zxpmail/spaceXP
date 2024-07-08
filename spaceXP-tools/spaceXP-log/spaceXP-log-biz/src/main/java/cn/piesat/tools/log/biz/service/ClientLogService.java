package cn.piesat.tools.log.biz.service;

import cn.piesat.framework.common.model.entity.OpLogEntity;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2023/10/13 10:47.
 *
 * @author zhouxp
 */
public interface ClientLogService  {
    Boolean add(OpLogEntity opLogEntity);
}
