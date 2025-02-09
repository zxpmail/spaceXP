package cn.piesat.framework.dynamic.datasource.utils;

import org.springframework.core.NamedThreadLocal;
import org.springframework.util.StringUtils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

/**
 * <p/>
 * {@code @description}: 动态数据源上下文工具类
 * <p/>
 * {@code @create}: 2025-02-09 14:21
 * {@code @author}: zhouxp
 */
public class DynamicDataSourceContextHolder {
    private static final ThreadLocal<Deque<String>> DATASOURCE_CONTEXT = NamedThreadLocal.withInitial(ArrayDeque::new);

    private DynamicDataSourceContextHolder() {
        throw new RuntimeException("禁止反射创建");
    }

    public static String getCurrentDataSource() {
        String peek = DATASOURCE_CONTEXT.get().peek();
        return Objects.isNull(peek) ? "" : peek;
    }

    public static String addDataSource(String dds) {
        String datasource = StringUtils.hasText(dds) ? dds : "";
        DATASOURCE_CONTEXT.get().push(datasource);
        return datasource;
    }

    public static void removeCurrentDataSource() {
        Deque<String> deque = DATASOURCE_CONTEXT.get();
        deque.poll();
        if (deque.isEmpty()) {
            DATASOURCE_CONTEXT.remove();
        }
    }
}
