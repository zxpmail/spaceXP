package cn.piesat.framework.common.utils;

import com.alibaba.ttl.TransmittableThreadLocal;


import javax.crypto.Cipher;
import java.util.ArrayDeque;
import java.util.Deque;


/**
 * <p/>
 * {@code @description}: kafka线程上下文工具类
 * <p/>
 * {@code @create}: 2025-01-21 13:12
 * {@code @author}: zhouxp
 */
public class AesContextHolder {

    private static final ThreadLocal<Deque<Cipher>> CONTEXT = new TransmittableThreadLocal<Deque<Cipher>>() {
        @Override
        protected Deque<Cipher> initialValue() {
            return new ArrayDeque<>();
        }
    };
    /**
     * 获得当前线程Cipher
     */
    public static Cipher get() {
        return CONTEXT.get().peek();
    }

    /**
     * 设置当前线程Cipher
     */
    public static void push(Cipher cipher) {
        CONTEXT.get().push(cipher);
    }

    /**
     * 清空当前线程数据源
     * <p>
     * 如果当前线程是连续切换Cipher 只会移除掉当前线程的Cipher
     * </p>
     */
    public static void poll() {
        Deque<Cipher> deque = CONTEXT.get();
        deque.poll();
        if (deque.isEmpty()) {
            CONTEXT.remove();
        }
    }

    /**
     * 强制清空本地线程
     * <p>
     * 防止内存泄漏，如手动调用了push可调用此方法确保清除
     * </p>
     */
    public static void clear() {
        CONTEXT.remove();
    }
}
