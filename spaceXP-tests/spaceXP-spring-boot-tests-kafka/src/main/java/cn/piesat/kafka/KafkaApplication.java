package cn.piesat.kafka;



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
        System.setProperty(KafkaConstant.ENCRYPTION_TOPICS, "parse_telemetry_real_time_topic");
        System.setProperty(KafkaConstant.ENCRYPTION_KEY, "1今晚打老虎");
        System.setProperty(KafkaConstant.ENCRYPTION_IV, "45a3830d-2e10-41");
        SpringApplication.run(KafkaApplication.class,args);
    }
}
