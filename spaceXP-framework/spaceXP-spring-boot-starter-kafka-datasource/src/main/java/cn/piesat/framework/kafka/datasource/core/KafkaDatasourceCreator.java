package cn.piesat.framework.kafka.datasource.core;

import cn.piesat.framework.kafka.datasource.model.DataSourceEntity;
import cn.piesat.framework.kafka.datasource.model.ExtraEntity;
import cn.piesat.framework.kafka.datasource.model.FactoryFilterEntity;
import cn.piesat.framework.kafka.datasource.properties.KafkaConfigProperties;
import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.GenericMessageListenerContainer;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
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
public class KafkaDatasourceCreator implements InitializingBean {
    private final KafkaConfigProperties kafkaConfigProperties;

    /**
     * springBean 工厂
     */
    @Resource
    private DefaultListableBeanFactory beanFactory;

    public KafkaDatasourceCreator(KafkaConfigProperties kafkaConfigProperties) {
        this.kafkaConfigProperties = kafkaConfigProperties;
    }

    @Override
    public void afterPropertiesSet() {
        createKafkaInstance();
    }

    /**
     * 创建kafka实例
     */
    private void createKafkaInstance() {
        if (!CollectionUtils.isEmpty(kafkaConfigProperties.getServer())) {
            for (Map.Entry<String, DataSourceEntity> dataSourceEntity : kafkaConfigProperties.getServer().entrySet()) {
                newConsumerInstance(dataSourceEntity.getValue());
                newProducerInstance(dataSourceEntity.getValue());
            }
        }
    }

    /**
     * 实例化生产者，以配置的server下面的producer的数量为基准
     */
    private void newProducerInstance(DataSourceEntity dataSourceEntity) {
        if (!CollectionUtils.isEmpty(dataSourceEntity.getProducer()) && StringUtils.hasText(dataSourceEntity.getProducerBeanName()) && dataSourceEntity.getProducer().size() > 6) {
            stringToClass(dataSourceEntity.getProducer());
            DefaultKafkaProducerFactory<String, String> defaultKafkaProducerFactory = new DefaultKafkaProducerFactory<>(dataSourceEntity.getProducer());
            String producerBeanName = dataSourceEntity.getProducerBeanName();
            KafkaTemplate<String, String> stringStringKafkaTemplate = new KafkaTemplate<>(defaultKafkaProducerFactory);
            stringStringKafkaTemplate.setBeanName(producerBeanName);
            beanFactory.registerSingleton(producerBeanName, stringStringKafkaTemplate);
            log.info("create kafka kafkaTemplate ,bean id is {} , factory config  is {}", producerBeanName, stringStringKafkaTemplate.getProducerFactory().getConfigurationProperties());
        }
    }


    /**
     * 由于配置在yml map中的对象默认识别出字符串，需要做下转换
     */
    @SneakyThrows
    private void stringToClass(Map<String, Object> map) {
        for (Map.Entry<String, Object> e : map.entrySet()) {
            if (e.getValue() instanceof String && ((String) e.getValue()).contains("org.apache.kafka.common")) {
                map.put(e.getKey(), Class.forName((String) e.getValue()));
            }
        }
    }

    /**
     * 例化消费端， 以配置的consumerExtra为基准
     */
    @SneakyThrows
    private void newConsumerInstance(DataSourceEntity dataSourceEntity) {
        if (!CollectionUtils.isEmpty(dataSourceEntity.getConsumer()) && dataSourceEntity.getConsumer().size() > 3) {
            for (Map.Entry<String, ExtraEntity> entry : dataSourceEntity.getConsumerExtra().entrySet()) {
                ConcurrentKafkaListenerContainerFactory<Integer, String> kafkaListenerContainerFactory;
                if (CollectionUtils.isEmpty(dataSourceEntity.getConsumerExtra())) {
                    kafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();
                    kafkaListenerContainerFactory.setAckDiscarded(entry.getValue().getAckDiscarded());
                    kafkaListenerContainerFactory.setConcurrency(entry.getValue().getConcurrency());
                    kafkaListenerContainerFactory.setBatchListener(entry.getValue().getBatchListener());
                    kafkaListenerContainerFactory.getContainerProperties().setPollTimeout(entry.getValue().getPollTimeout());
                } else {
                    Class<? extends GenericMessageListenerContainer<?, ?>> consumerFactory = entry.getValue().getConsumerFactory();
                    kafkaListenerContainerFactory = (ConcurrentKafkaListenerContainerFactory<Integer, String>) consumerFactory.newInstance();
                }
                stringToClass(dataSourceEntity.getConsumer());
                DefaultKafkaConsumerFactory<Object, Object> consumerFactory = new DefaultKafkaConsumerFactory<>(dataSourceEntity.getConsumer());
                kafkaListenerContainerFactory.setConsumerFactory(consumerFactory);
                if (entry.getValue().getAckDiscarded()) {
                    setFilterFactory(kafkaListenerContainerFactory, entry.getValue().getConsumerFactoryFilter());
                }
                beanFactory.registerSingleton(entry.getKey(), kafkaListenerContainerFactory);
                log.info("create kafka consumer ,bean id is {} , properties is {} , factory config is {}", entry.getKey(), kafkaListenerContainerFactory.getContainerProperties(), kafkaListenerContainerFactory.getConsumerFactory().getConfigurationProperties());
            }
        }
    }

    /**
     * 设置工厂过滤策略
     */
    @SneakyThrows
    private void setFilterFactory(ConcurrentKafkaListenerContainerFactory<Integer, String> kafkaListenerContainerFactory, FactoryFilterEntity consumerFactoryFilter) {
        if (!ObjectUtils.isEmpty(consumerFactoryFilter)) {
//           代表配置了过滤策略
            if (!ObjectUtils.isEmpty(consumerFactoryFilter.getStrategy())) {
//               代表使用了自定义策略
                kafkaListenerContainerFactory.setRecordFilterStrategy(consumerFactoryFilter.getStrategy().newInstance());
            } else if (!consumerFactoryFilter.getFilter().isEmpty()) {
                kafkaListenerContainerFactory.setRecordFilterStrategy((ConsumerRecord<Integer, String> consumerRecord) -> {
                    try {
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
    private boolean judgeFieldMap(Object entity, Map<String, String> filters) {
        if (entity == null || filters == null || filters.isEmpty()) {
            // 对输入参数的空检查
            log.info("Entity and filters cannot be null or empty.");
            return false;
        }

        Class<?> aClass = entity.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();

        // 使用一个布尔值记录是否所有条件都满足
        boolean allConditionsMet = true;
        for (String filterKey : filters.keySet()) {
            boolean conditionMetForCurrentKey = false;
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                try {
                    // 假设getaBoolean方法已经实现，这里不做改动
                    conditionMetForCurrentKey = getBoolean(entity, filters, declaredField, filterKey);
                    if (conditionMetForCurrentKey) {
                        break; // 代表这个key满足条件了，不需要再循环了
                    }
                } catch (Exception e) {
                    // 异常处理逻辑（这里简单地打印异常堆栈）
                    e.printStackTrace();
                }
            }
            if (!conditionMetForCurrentKey) {
                allConditionsMet = false;
                break; // 如果有一个key的条件不满足，可以直接退出当前循环
            }
        }

        // 返回逻辑与注释一致：如果所有条件都满足，则返回false；否则，返回true（丢弃消息）
        return !allConditionsMet;
    }

    /**
     * 计算每个key是否满足条件，满足就返回true ,否则返回false
     */
    private boolean getBoolean(Object targetObject, Map<String, String> filterMap, Field declaredField, String filterKey) {
        // 检查入参的合法性
        if (targetObject == null || filterMap == null || declaredField == null || filterKey == null) {
            log.info("Invalid null argument provided.");
            return false;
        }

        if (declaredField.getName().equals(filterKey)) {
            try {
                Object fieldValue = declaredField.get(targetObject); // 获取字段值
                if (fieldValue == null) {
                    return false; // 如果字段值为null，则直接返回false，这取决于业务预期
                }

                String[] values = StringUtils.split(filterMap.get(filterKey), ",");
                if(values ==null || values.length<1){
                    return  false;
                }
                Set<String> valueSet = new HashSet<>(Arrays.asList(values));
                // 如果fieldValue是字符串，直接进行判断；此处省略了对fieldValue类型强转的冗余代码
                if (fieldValue instanceof String) {
                    return valueSet.contains(fieldValue.toString());
                }
            } catch (IllegalAccessException e) {
                String errorMessage = "反射获取字段失败, 对象为 " + targetObject + " 字段为 " + declaredField.getName() + " 过滤的值为 " + filterKey;
                log.error(errorMessage, e);
                // 考虑是否需要根据异常情况做不同的处理
                return false;
            }
        }
        return false;
    }
}
