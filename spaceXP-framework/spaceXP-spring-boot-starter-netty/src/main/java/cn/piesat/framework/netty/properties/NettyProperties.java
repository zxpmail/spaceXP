package cn.piesat.framework.netty.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;

import java.util.List;


/**
 * <p/>
 * {@code @description}: netty属性配置类
 * <p/>
 * {@code @create}: 2024-10-14 15:02
 * {@code @author}: zhouxp
 */
@Data
@ConfigurationProperties(prefix = "space.netty")
public class NettyProperties {

    private TcpClientItem tcpClient;

    @Data
    public final static class TcpClientItem {
        /**
         * 服务器地址
         */
        private String serverHost;
        /**
         * 服务器端口
         */
        private Integer serverPort;
        /**
         * 最大重连次数
         */
        private int retryMaxCount = 5;

        /**
         * 初始重连延迟时间（毫秒）
         */
        private long reconnectDelayMs = 1000;
    }

    /**
     * 数据项,不包含version位
     */
    private MessageItem messageItem;

    @Data
    public final static class MessageItem {
        /**
         * 版本占位数
         */
        private Integer versionBytes = 1;
        /**
         * 包长占位数
         */
        private Integer packetSize = 32;
        /**
         * 版本值
         */
        private long versionValue = 0x80L;

        /**
         * 版本类型
         */
        private String versionType;

        /**
         * 最大包长
         */
        private int maxPacketSize = 1024;

        private List<DataItem> items = new ArrayList<>();
    }

    @Data
    public final static class DataItem {
        /**
         * 数据项名称
         */
        private String name;
        /**
         * 数据项类型 参照TypeEnum
         */
        private String type;
        /**
         * 数据占用几个字节
         */
        private Integer bytes;
        /**
         * 是否忽略此数据项
         */
        private Boolean isKipped = false;

        /**
         * 是否是包长字段
         */
        private Boolean isPackageLength = false;

    }
}
