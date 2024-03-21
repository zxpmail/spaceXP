package cn.piesat.framework.kafka.datasource.model;

import lombok.Data;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p/>
 * {@code @description}  :工厂过滤实体
 * <p/>
 * <b>@create:</b> 2024-03-21 10:30.
 *
 * @author zhouxp
 */
@Data
public class FactoryFilterEntity {
    /**
     * 要序列化成的对象
     */
    private Class<?> serializerClass;
    /**
     * 指定自定义策略
     */
    private Class<? extends RecordFilterStrategy<?,?>> strategy;
    /**
     * 过滤的字段集合，key为要对比的字段，value为比较的值，支持逗号分隔
     */
    private Map<String, String> filter = new LinkedHashMap<>();
}
