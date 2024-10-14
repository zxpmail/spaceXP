package cn.piesat.framework.netty.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    private Set<Item> items = new HashSet<>();

    @Data
    static class Item {
        /**
         * 数据项名称
         */
        private String name;
        /**
         * 数据项类型
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
            Item item = (Item) o;
            return name.equals(item.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }
}
