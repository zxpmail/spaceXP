package cn.piesat.framework.dynamic.datasource.transaction;

/**
 * <p/>
 * {@code @description}:全局事务ID上下文工具类
 * <p/>
 * {@code @create}: 2025-02-12 15:22
 * {@code @author}: zhouxp
 */
public class TransactionGlobalIdContextHolder {
    private static final ThreadLocal<String> TX_GLOBAL_ID = new ThreadLocal<>();

    /**
     * 获取全局事务id
     */
    public static String getTxGlobalId() {
        return TX_GLOBAL_ID.get();
    }

    public static void setTxGlobalId(String txId) {
        TX_GLOBAL_ID.set(txId);
    }

    public static void removeGlobalId() {
        TX_GLOBAL_ID.remove();
    }
}
