package cn.piesat.framework.dynamic.datasource.transaction;

import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * <p/>
 * {@code @description}: 事务工具类
 * <p/>
 * {@code @create}: 2025-02-12 15:31
 * {@code @author}: zhouxp
 */
public class TransactionUtil {
    public static void startTransaction() {
        String txGlobalId = TransactionGlobalIdContextHolder.getTxGlobalId();
        if (!StringUtils.hasText(txGlobalId)) {
            txGlobalId = UUID.randomUUID().toString();
            TransactionGlobalIdContextHolder.setTxGlobalId(txGlobalId);
        }
    }

    public static void rollback() {
        TransactionConnectionContextHolder.rollback();
    }

    public static void commit() {
        TransactionConnectionContextHolder.commit();
    }
}
