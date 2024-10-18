package cn.piesat.test.netty.command;

import cn.piesat.framework.common.context.AbstractValidateItem;
import cn.piesat.test.netty.client.TcpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <p/>
 * {@code @description}:
 * <p/>
 * {@code @create}: 2024-10-18 17:28
 * {@code @author}: zhouxp
 */
@Slf4j
@Component
public class ValidateNetty extends AbstractValidateItem {
    @Resource
    private TcpClient tcpClient;
    @Override
    public void validate() {
        log.info("client started................" );
        tcpClient.start();
        // 执行你的逻辑
        log.info("end tasks.......................");
    }
}
