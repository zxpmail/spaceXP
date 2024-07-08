package cn.piesat.tools.log.biz.service.impl;

import cn.piesat.framework.common.model.entity.OpLogEntity;
import cn.piesat.framework.common.utils.CopyBeanUtils;
import cn.piesat.tools.log.biz.constant.LogConstants;
import cn.piesat.tools.log.biz.model.entity.LogDO;
import cn.piesat.tools.log.biz.model.entity.LoginLogDO;
import cn.piesat.tools.log.biz.service.ClientLogService;
import cn.piesat.tools.log.biz.service.LogService;
import cn.piesat.tools.log.biz.service.LoginLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p/>
 * {@code @description}  :接受远程日志并存储
 * <p/>
 * <b>@create:</b> 2023/10/13 10:46.
 *
 * @author zhouxp
 */
@Service
@RequiredArgsConstructor
public class ClientLogServiceImpl  implements ClientLogService {
    private final LoginLogService loginLogService;
    private final LogService logService;


    @Override
    public Boolean add(OpLogEntity opLogEntity) {
        if(opLogEntity.getCode().equals(LogConstants.LOGIN) || opLogEntity.getCode().equals(LogConstants.LOGOUT) ){
            LoginLogDO loginLogDO = new LoginLogDO();
            loginLogDO.setLogined(opLogEntity.getCode().equals(LogConstants.LOGIN)?0:1);
            loginLogDO.setIp(opLogEntity.getIp());
            loginLogDO.setUserName(opLogEntity.getUserName());
            loginLogDO.setBrowser(opLogEntity.getBrowser());
            loginLogDO.setType(opLogEntity.getType());
            loginLogDO.setMsg(opLogEntity.getExDetail());
            loginLogService.save(loginLogDO);
        }else {
            logService.save(CopyBeanUtils.copy(opLogEntity, LogDO::new));
        }
        return null;
    }
}
