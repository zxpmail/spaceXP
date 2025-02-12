package cn.piesat.framework.dynamic.datasource.transaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * <p/>
 * {@code @description}: 事务上下文工具类
 * <p/>
 * {@code @create}: 2025-02-12 17:06
 * {@code @author}: zhouxp
 */
public class TransactionContextUtil {
    /**
     * 开启事务
     */
    public static void doStartTx(Connection connection) throws SQLException {
        connection.setAutoCommit(false);
    }

    /**
     * 提交事务
     */
    public static void doCommitTx(Connection connection) throws SQLException {
        connection.commit();
    }

    /**
     * 事务回滚
     */
    public static void doRollbackTx(Connection connection) throws SQLException {
        connection.rollback();
    }
}
