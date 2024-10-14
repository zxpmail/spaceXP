package cn.piesat.framework.netty.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    private Set<DataItem> items = new HashSet<>();

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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DataItem item = (DataItem) o;
            return name.equals(item.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }
}
