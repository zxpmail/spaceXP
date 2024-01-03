package cn.piesat.framework.dynamic.datasource.utils;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.springframework.util.StringUtils;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * <p/>
 * {@code @description}  : //此类提供线程局部变量。这些变量不同于它们的正常对应关系是每个线程访问一个线程(通过get、set方法),有自己的独立初始化变量的副本。
 * <p/>
 * <b>@create:</b> 2023/12/9 11:02.
 *
 * @author zhouxp
 */
public class DataSourceContextHolder {
    private static final ThreadLocal<Deque<String>> DATASOURCE_HOLDER = new TransmittableThreadLocal<Deque<String>>() {
        @Override
        protected Deque<String> initialValue() {
            return new ArrayDeque<>();
        }
    };

    /**
     * 获得当前线程数据源
     *
     * @return 数据源名称
     */
    public static String peek() {
        return DATASOURCE_HOLDER.get().peek();
    }

    /**
     * 设置当前线程数据源
     * <p>
     * 如非必要不要手动调用，调用后确保最终清除
     * </p>
     *
     * @param ds 数据源名称
     * @return 数据源名称
     */
    public static String push(String ds) {
        DATASOURCE_HOLDER.get().push(StringUtils.hasText(ds) ? ds : "");
        return ds;
    }

    /**
     * 清空当前线程数据源
     * <p>
     * 如果当前线程是连续切换数据源 只会移除掉当前线程的数据源名称
     * </p>
     */
    public static void poll() {
        Deque<String> deque = DATASOURCE_HOLDER.get();
        deque.poll();
        if (deque.isEmpty()) {
            DATASOURCE_HOLDER.remove();
        }
    }

    /**
     * 强制清空本地线程
     * <p>
     * 防止内存泄漏，如手动调用了push可调用此方法确保清除
     * </p>
     */
    public static void clear() {
        DATASOURCE_HOLDER.remove();
    }
}
