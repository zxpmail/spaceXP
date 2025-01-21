package cn.piesat.framework.kafka.utils;

import cn.piesat.framework.common.utils.AesUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.piesat.framework.kafka.constants.KafkaConstant.ENCRYPTION_IV;
import static cn.piesat.framework.kafka.constants.KafkaConstant.ENCRYPTION_KEY;
import static cn.piesat.framework.kafka.constants.KafkaConstant.ENCRYPTION_TOPICS;

/**
 * <p/>
 * {@code @description}: 初始化工具类
 * <p/>
 * {@code @create}: 2025-01-21 14:09
 * {@code @author}: zhouxp
 */
@Slf4j
public class KafkaInitConfigureUtil {

    public static void initConfigure(Set<String> topicSet){
        try {
            // 获取并验证 topics 配置
            String topics = System.getProperty(ENCRYPTION_TOPICS);
            if (StringUtils.hasText(topics)) {
                String[] topicsArray = topics.split(",");
                Set<String> validTopics = Arrays.stream(topicsArray)
                        .map(String::trim)
                        .filter(StringUtils::hasText)
                        .collect(Collectors.toCollection(HashSet::new));
                topicSet.addAll(validTopics);
            }

            // 获取并验证 key 和 iv 配置
            String key = System.getProperty(ENCRYPTION_KEY);
            String iv = System.getProperty(ENCRYPTION_IV);

            if (!isValidKeyAndIv(key, iv)) {
                throw new IllegalArgumentException("Invalid encryption key or IV");
            }

            AesUtils.init(key, iv);
            log.info("Configuration initialized successfully.");
        } catch (Exception e) {
            log.error("Failed to initialize configuration: {} " , e.getMessage(), e);
            throw new RuntimeException("Failed to initialize configuration", e);
        }
    }
    private static boolean isValidKeyAndIv(String key, String iv) {
        return StringUtils.hasText(key) && StringUtils.hasText(iv);
    }
}
