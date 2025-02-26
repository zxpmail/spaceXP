package cn.piesat.framework.mybatis.plus.external.core;

import lombok.Data;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * <p/>
 * {@code @description}: 包装请求类
 * <p/>
 * {@code @create}: 2025-02-18 16:09
 * {@code @author}: zhouxp
 */
@Data
public final class WrapRequest<T,R> {
    /**
     * 标识ID
     */
    String requestId;
    /**
     * 请求参数
     */
    T params;
    /**
     * 相应结果
     */
    LinkedBlockingQueue<R> response;
}
