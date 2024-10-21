package cn.piesat.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.record.TimestampType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p/>
 * Kafka 消费者 · 接收消息.
 * 1、topics：监听的主题，可以写死，也可以通过全局配置文件配置取值，如 @KafkaListener(topics = {"${my.kafka.topic-name}"})
 * 2、系统中定义了消费者(@KafkaListener)时，启动服务后，如果连不上kafka服务器则会输出大量的警告日志，但是不会报错。
 * 不是每个环境都启动了kafka服务，所以当没有配置消费者组id的时候，本类不交由Spring容器初始化，不再监听消息。
 *
 * <p/>
 * {@code @create}: 2024-10-21 10:22
 * {@code @author}: zhouxp
 */

@Component
@ConditionalOnProperty(prefix = "spring.kafka.consumer", name = "group-id")
public class Kafka1Consumer {

    private static final Logger log = LoggerFactory.getLogger(Kafka1Consumer.class);

    /**
     * 监听指定主题上的消息，topics 属性是一个字符串数组，可以监听多个主题。
     * * id ：用于唯一标识此消费者监听器，不同方法上此注解的id必须唯一，不设置时，会自动生成
     * * topics：监听的主题，可以写死，也可以通过全局配置文件配置取值，如 @KafkaListener(topics = {"${my.kafka.topic-name}"})
     *
     * @param record ：消息记录对象，包含消息正文、主题名、分区号、偏移量、时间戳等等
     */
    @KafkaListener(id = "basicConsumer", topics = {"car-infos", "basic-info", "helloWorld", "bgt.basic.agency.frame.topic"})
    public void messageListener1(ConsumerRecord<?, ?> record) {
        /**
         * headers：消息头信息
         * offset：此记录在相应的 Kafka 分区中的位置。
         * partition：记录所在的分区
         * serializedKeySize：序列化的未压缩密钥的大小（以字节为单位），如果 key为 null，则返回的大小为 -1
         * serializedValueSize：序列化的未压缩值（消息正文）的大小（以字节为单位，record.value().getBytes().length）。如果值为 null，则返回的大小为 -1
         * timestamp：记录的时间戳
         * TimestampType：记录的时间戳类型
         * topic：接收此记录的主题
         * value：消息内容
         */
        Headers headers = record.headers();
        long offset = record.offset();
        int partition = record.partition();
        int serializedKeySize = record.serializedKeySize();
        int serializedValueSize = record.serializedValueSize();
        long timestamp = record.timestamp();
        TimestampType timestampType = record.timestampType();
        String topic = record.topic();
        Object value = record.value();

        System.out.println("收到消息：");
        System.out.println("\theaders=" + headers);
        System.out.println("\toffset=" + offset);
        System.out.println("\tpartition=" + partition);
        System.out.println("\tserializedKeySize=" + serializedKeySize);
        System.out.println("\tserializedValueSize=" + serializedValueSize);
        System.out.println("\ttimestamp=" + timestamp);
        System.out.println("\ttimestampType=" + timestampType);
        System.out.println("\ttopic=" + topic);
        System.out.println("\tvalue=" + value);
    }

    /**
     * 批量消费时，必须使用 List 接收，否则会抛异常。
     * 即如果配置文件配置的是批量消费(spring.kafka.listener.type=batch)，则监听时必须使用 list 接收
     * 反之如果配置是单条消息消费，则不能使用 list 接收，否则也会异常.
     *
     * @param records
     */
    @KafkaListener(topics = "batch-msg")
    public void messageListener2(List<ConsumerRecord<?, ?>> records) {
        System.out.println(">>>批量消费返回条数，records.size()=" + records.size());
        int count = 0;
        for (ConsumerRecord<?, ?> record : records) {
            System.out.println("\t消息" + (++count) + "：" + record.value());
        }
    }

    /**
     * 消费消息并转换。SendTo 可以标注在类上，此时对类中的所有方法有效，方法的返回值表示转发的消息内容。
     *
     * @param record
     * @return
     */
    @KafkaListener(topics = {"sendTo"})
    @SendTo("car-infos")
    public String messageListener3(ConsumerRecord<?, ?> record) {
        System.out.println("消费单条消费并转发：" + record.value() + "," + record.timestamp());
        return record.value().toString();
    }

    /**
     * 单位一体化编码与名称更正消息监听
     * 约定更正接口返回结果监听的主题为：basic.kafka.syncAgencyStatInfo.reply
     *
     * @param recordList
     */
    @KafkaListener(topics = {"${app.kafka.topics.agency:topic3}"})
    public void syncAgencyStatInfoMsgListener(List<ConsumerRecord<String, String>> recordList) {
        for (ConsumerRecord<String, String> record : recordList) {
            log.info("监听单位一体化编码与名称更正消息：{}", record);
            try {
                System.out.println("消息处理.....");
            } catch (Exception e) {
                log.error("单位一体化编码与名称更正消息消费失败：{}", e.getMessage(),e);
            }
        }
    }

}

