package cn.piesat.tests.log.service;


import cn.piesat.framework.common.exception.BaseException;
import cn.piesat.framework.common.model.entity.OpLogEntity;
import cn.piesat.framework.common.model.enums.BusinessEnum;
import cn.piesat.framework.common.model.enums.CommonResponseEnum;
import cn.piesat.framework.log.annotation.OpLog;
import cn.piesat.framework.log.service.ExecuteLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * <p/>
 * {@code @description}  :测试日志
 * <p/>
 * <b>@create:</b> 2022/12/9 17:25.
 *
 * @author zhouxp
 */
@Service
@Slf4j
public class TestService implements ExecuteLogService {

    @Override
    public void exec(OpLogEntity opLogEntity) {
        System.out.println(opLogEntity);
        System.out.println(Thread.currentThread());
    }


    @OpLog(op = BusinessEnum.CLEAN,description = "测试")
    public void testService()  {
        log.error("log 日志1");
        log.info("log 日志2 ");
        throw new BaseException(CommonResponseEnum.ERROR);
    }
}
