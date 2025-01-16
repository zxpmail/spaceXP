package cn.piesat.kafka;


import cn.piesat.framework.common.utils.AesUtils;
import cn.piesat.framework.kafka.constants.KafkaConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p/>
 * {@code @description}  :启动程序
 * <p/>
 * <b>@create:</b> 2022/11/9 17:26.
 *
 * @author zhouxp
 */
@SpringBootApplication
public class KafkaApplication {
    public static void main(String[] args) {
        System.setProperty(KafkaConstant.IGNORE_TOPICS, "my*");
        AesUtils.init("0123456789abcdef","Abcdefghijklmnop");
        SpringApplication.run(KafkaApplication.class,args);
    }
}
