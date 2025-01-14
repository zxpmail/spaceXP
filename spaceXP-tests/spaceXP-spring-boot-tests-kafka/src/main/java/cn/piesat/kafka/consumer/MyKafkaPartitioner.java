package cn.piesat.kafka.consumer;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

/**
 * <p/>
 * {@code @description}: 自定义分区规则，一旦自定义了分区规则，就不会再走 kafka 默认的分区规则
 * <p/>
 * {@code @create}: 2024-10-21 10:17
 * {@code @author}: zhouxp
 */

public class MyKafkaPartitioner implements Partitioner {
    /**
     * 计算给定记录的分区，发送消息到 kafka 服务器之前，都会先走这里进行计算目标分区，即将消息发送到具体的哪个分区
     *
     * @param topic      ：主题名称
     * @param key        ：要分区的键（如果没有键，则为null）
     * @param keyBytes   ：要分区的序列化键（如果没有键，则为null）
     * @param value      ：要分区的值或null，健可以有可无，值才是真正的消息内容
     * @param valueBytes ：要分区的序列化值或null
     * @param cluster    ：当前集群信息
     */
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        // 返回的整数值就是表示生产者将消息发送到的分区
        // 具体的规则可以根据自身需要设置
        System.out.println("发送消息：" + value);
        System.out.println("指定分区为：" + 0);
        return 0;
    }

    /**
     * 在分区程序关闭时调用
     */
    @Override
    public void close() {

    }

    /**
     * 使用给定的键值对配置此类
     *
     * @param configs
     */
    @Override
    public void configure(Map<String, ?> configs) {

    }
}