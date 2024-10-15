package cn.piesat.framework.netty.core;

/**
 * <p/>
 * {@code @description}:
 * <p/>
 * {@code @create}: 2024-10-15 13:20
 * {@code @author}: zhouxp
 */
public interface ErrorLogService {
    default void send(byte[] data){}
}
