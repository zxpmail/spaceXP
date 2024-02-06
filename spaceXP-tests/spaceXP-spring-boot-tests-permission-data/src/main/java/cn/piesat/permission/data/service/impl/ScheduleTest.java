package cn.piesat.permission.data.service.impl;

import cn.piesat.permission.data.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;



/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2024/1/31 14:58.
 *
 * @author zhouxp
 */
@Service
@Slf4j
public class ScheduleTest {
    @Resource
    private UserService userService;
    @Scheduled(cron = "*/5 * * * * ?")
    public void scheduled() {

        log.info(userService.info(1L).toString());;
    }
}
