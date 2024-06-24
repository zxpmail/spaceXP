package cn.piesat.framework.kafka.datasource.core;

import cn.piesat.framework.kafka.datasource.properties.KafkaConfigProperties;
import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;


import java.lang.reflect.Field;

import java.util.HashMap;
import java.util.HashSet;

import java.util.Map;
import java.util.Objects;

import java.util.Set;


/**
 * <p/>
 * {@code @description}  : kafka数据源创建器
 * <p/>
 * <b>@create:</b> 2024-03-21 10:53.
 *
 * @author zhouxp
 */
@Slf4j
public class KafkaCreator implements InitializingBean {
    private final KafkaConfigProperties kafkaConfigProperties;

    /**
     * springBean 工厂
     */
    @Resource
    private DefaultListableBeanFactory beanFactory;

    public KafkaCreator(KafkaConfigProperties kafkaConfigProperties) {
        this.kafkaConfigProperties = kafkaConfigProperties;
    }

    @Override
    public void afterPropertiesSet() {
        try {
            createKafkaInstance();
        } catch (Exception e) {
            log.error("初始化kafka config失败 " + e);
        }
    }

    /**
     * 创建kafka实例
     */
    private void createKafkaInstance() {
        if (!CollectionUtils.isEmpty(kafkaConfigProperties.getServers())) {
            for (Map.Entry<String, KafkaConfigProperties.KafkaConfig> kafkaConfig : kafkaConfigProperties.getServers().entrySet()) {
                newConsumerInstance(kafkaConfig.getValue());
                newProducerInstance(kafkaConfig.getValue());
            }
        }
    }

    /**
     * 实例化生产者，以配置的server下面的producer的数量为基准
     */
    private void newProducerInstance(KafkaConfigProperties.KafkaConfig kafkaConfig) {
        if (!CollectionUtils.isEmpty(kafkaConfig.getProducer()) &&
                StringUtils.hasText(kafkaConfig.getProducerBeanName()) &&
                kafkaConfig.getProducer().size() > 6) {

            try {
                convertStringToClassIfNecessary(kafkaConfig.getProducer());
                // 使用外部配置来初始化KafkaProducerFactory，这里简化处理为直接使用dataSourceEntity.getProducer()中的信息
                DefaultKafkaProducerFactory<String, String> defaultKafkaProducerFactory = new DefaultKafkaProducerFactory<>(kafkaConfig.getProducer());
                String producerBeanName = kafkaConfig.getProducerBeanName();
                KafkaTemplate<String, String> stringStringKafkaTemplate = new KafkaTemplate<>(defaultKafkaProducerFactory);
                stringStringKafkaTemplate.setBeanName(producerBeanName);
                // 使用依赖注入的方式注册Bean
                beanFactory.registerSingleton(producerBeanName, stringStringKafkaTemplate);
                // 移除敏感信息的日志记录
                log.info("Kafka Template with bean name '{}' registered successfully", producerBeanName);
            } catch (Exception e) {
                // 异常处理逻辑
                log.error("Failed to register Kafka producer bean", e);
            }
        }
    }

    /**
     * 由于配置在yml map中的对象默认识别出字符串，需要做下转换
     */
    public void convertStringToClassIfNecessary(Map<String, Object> map) {
        map.replaceAll((key, value) -> {
            if (value instanceof String && ((String) value).contains("org.apache.kafka.common")) {
                try {
                    return Class.forName((String) value);
                } catch (ClassNotFoundException e) {
                    log.error("Failed to load class: {}", value);
                    return value;
                }
            }
            return value;
        });
    }

    /**
     * 例化消费端， 以配置的consumerExtra为基准
     */
    @SneakyThrows
    private void newConsumerInstance(KafkaConfigProperties.KafkaConfig kafkaConfig) {
        if (!CollectionUtils.isEmpty(kafkaConfig.getConsumer()) && kafkaConfig.getConsumer().size() > 3) {
            for (Map.Entry<String, KafkaConfigProperties.ConsumerExtraConfig> entry : kafkaConfig.getConsumerExtra().entrySet()) {
                ConcurrentKafkaListenerContainerFactory<Integer, String> kafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();
                if (Objects.isNull(entry.getValue().getConsumerFactory())) {
                    kafkaListenerContainerFactory.setAckDiscarded(entry.getValue().getAckDiscarded());
                    kafkaListenerContainerFactory.setConcurrency(entry.getValue().getConcurrency());
                    kafkaListenerContainerFactory.setBatchListener(entry.getValue().getBatchListener());
                    kafkaListenerContainerFactory.getContainerProperties().setPollTimeout(entry.getValue().getPollTimeout());
                } else {
                    Class<? extends KafkaListenerContainerFactory<?>> consumerFactory = entry.getValue().getConsumerFactory();
                    KafkaListenerContainerFactory<?> listenerContainerFactory = consumerFactory.newInstance();
                    if (listenerContainerFactory instanceof ConcurrentKafkaListenerContainerFactory) {
                        kafkaListenerContainerFactory = (ConcurrentKafkaListenerContainerFactory<Integer, String>) listenerContainerFactory;
                    } else {
                        log.error("Consumer factory instance is not of expected type.");
                    }

                }
                convertStringToClassIfNecessary(kafkaConfig.getConsumer());
                DefaultKafkaConsumerFactory<Object, Object> consumerFactory = new DefaultKafkaConsumerFactory<>(kafkaConfig.getConsumer());
                kafkaListenerContainerFactory.setConsumerFactory(consumerFactory);
                KafkaConfigProperties.ConsumerFactoryFilter factoryFilter = entry.getValue().getFactoryFilter();
                if (entry.getValue().getAckDiscarded()) {
                    setFilterFactory(kafkaListenerContainerFactory, factoryFilter);
                }
                beanFactory.registerSingleton(entry.getKey(), kafkaListenerContainerFactory);
                log.info("create kafka consumer ,bean id is {} , properties is {} , factory config is {}", entry.getKey(), kafkaListenerContainerFactory.getContainerProperties(), kafkaListenerContainerFactory.getConsumerFactory().getConfigurationProperties());
            }
        }
    }

    /**
     * 设置工厂过滤策略
     */
    @SuppressWarnings("rawtypes")
    private void setFilterFactory(ConcurrentKafkaListenerContainerFactory<Integer, String> kafkaListenerContainerFactory, KafkaConfigProperties.ConsumerFactoryFilter consumerFactoryFilter) {
        if (!ObjectUtils.isEmpty(consumerFactoryFilter)) {
            if (!ObjectUtils.isEmpty(consumerFactoryFilter.getStrategy())) {
                //代表使用了自定义策略
                RecordFilterStrategy recordFilterStrategy = null;
                try {
                    recordFilterStrategy = consumerFactoryFilter.getStrategy().newInstance();
                } catch (Exception e) {
                    log.error("create strategy instance error {}", e.getMessage());
                    return;
                }
                kafkaListenerContainerFactory.setRecordFilterStrategy(recordFilterStrategy);
            } else if (!consumerFactoryFilter.getFilter().isEmpty()) {

                kafkaListenerContainerFactory.setRecordFilterStrategy((ConsumerRecord<Integer, String> consumerRecord) -> {
                    try {
                        if (consumerRecord.value() == null || consumerFactoryFilter.getSerializerClass() == null) {
                            log.error("filter error value is null  or serializerClass is null");
                            return true;
                        }
                        Object o = JSONObject.parseObject(consumerRecord.value(), consumerFactoryFilter.getSerializerClass());
                        return judgeFieldMap(o, consumerFactoryFilter.getFilter());

                    } catch (Exception e) {
                        log.error("filter error topic is {},value is {}", consumerRecord.topic(), consumerRecord.value());
                        return true;
                    }
                });
            }
        }
    }

    /**
     * 计算多条件下的过滤规则
     */
    private boolean judgeFieldMap(Object o, Map<String, String> filterMap) throws IllegalAccessException {
        Class<?> aClass = o.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        Map<String, Object> fieldValues = new HashMap<>(); // 用于存储字段名和值

        // 提前获取所有字段值
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            String name = declaredField.getName();
            Object fieldValue = declaredField.get(o);
            if (!Objects.isNull(fieldValue) && fieldValue instanceof String) {
                fieldValues.put(name, fieldValue);
            }
        }

        // 使用HashSet优化性能
        Set<String> filterKeys = new HashSet<>(filterMap.keySet());
        int matchCount = 0; // 匹配计数

        // 遍历字段值，检查是否匹配
        for (Map.Entry<String, Object> entry : fieldValues.entrySet()) {
            String fieldName = entry.getKey();
            String fieldValue = entry.getValue().toString();
            if (filterKeys.contains(fieldName) && filterMap.get(fieldName).contains(fieldValue)) {
                matchCount++;
            }
        }

        // 调整返回逻辑，使得匹配成功返回true
        return matchCount != filterMap.size();
    }
}
