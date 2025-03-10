package cn.zhouxp.tests.id;

import cn.zhouxp.framework.id.generate.service.IdGenerateService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import java.util.HashSet;

/**
 * <p/>
 * {@code @description}  :启动程序
 * <p/>
 * <b>@create:</b> 2022/11/9 17:26.
 *
 * @author zhouxp
 */
@SpringBootApplication
public class IdGenerateApplication implements CommandLineRunner {

    @Resource
    private IdGenerateService idGenerateService;

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(IdGenerateApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        HashSet<Long> idSet = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            Thread.sleep(10);
            Long id = idGenerateService.generateId("user_id");
            idSet.add(id);
        }
        System.out.println(idSet.size());
    }
}
