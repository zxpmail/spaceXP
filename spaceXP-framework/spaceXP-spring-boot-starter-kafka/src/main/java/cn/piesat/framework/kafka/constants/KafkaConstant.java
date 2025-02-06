package cn.piesat.framework.kafka.constants;

import java.util.regex.Pattern;

/**
 * <p/>
 * {@code @description}: kafka常量
 * <p/>
 * {@code @create}: 2025-01-15 10:58
 * {@code @author}: zhouxp
 */
public interface KafkaConstant {
    String ENCRYPTION_TOPICS = "space.kafka.encryptionTopics";

    String ENCRYPTION_KEY = "space.kafka.key";

    String ENCRYPTION_IV = "space.kafka.iv";

    /**
     * 加密比例 1表示数据全加密 2表示一半 3表示三分之一等
     */
    String ENCRYPTION_RATE ="space.kafka.encryptionRate";
}
