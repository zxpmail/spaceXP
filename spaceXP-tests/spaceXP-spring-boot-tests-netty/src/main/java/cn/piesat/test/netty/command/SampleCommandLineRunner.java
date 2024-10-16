package cn.piesat.test.netty.command;

import cn.piesat.test.netty.client.TcpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * <p/>
 * {@code @description}: 启动程序
 * <p/>
 * {@code @create}: 2024-10-16 15:57
 * {@code @author}: zhouxp
 */
@Component
@Slf4j
public class SampleCommandLineRunner implements CommandLineRunner {

    @Override
    public void run(String... args)  {
        log.info("Application started with arguments: {} " , String.join(", ", args));
        int port = 9001;
        new TcpClient("127.0.0.1",port).start();
        // 执行你的逻辑
        log.info("Performing initialization tasks...");
    }
}
