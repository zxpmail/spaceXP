package cn.piesat.framework.dynamic.datasource.transaction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NamedThreadLocal;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

/**
 * <p/>
 * {@code @description}:事务Connection上下文工具类
 * <p/>
 * {@code @create}: 2025-02-12 16:31
 * {@code @author}: zhouxp
 */
@Slf4j
public class TransactionConnectionContextHolder {
    private static final ThreadLocal<ConcurrentMap<String, Connection>> CONNECTION = NamedThreadLocal.withInitial(ConcurrentHashMap::new);

    /**
     * 放入数据源
     *
     * @param dsName     数据源名称
     * @param connection 数据源连接
     * @return 返回true表明第一次存入，返回false表明之前已经存过
     */
    public static Boolean putConnection(String dsName, Connection connection) {
        ConcurrentMap<String, Connection> connectionMap = CONNECTION.get();
        if (connection == null) {
            log.warn("{}：数据源为空！", dsName);
            return false;
        }
        Connection existingConnection = connectionMap.putIfAbsent(dsName, connection);
        return existingConnection == null;
    }

    /**
     * 获取数据源链接
     *
     * @param dsName 数据源名称
     * @return 数据源链接
     */
    public static Connection getConnection(String dsName) {
        return CONNECTION.get().get(dsName);
    }

    /**
     * 提交事务内所有连接
     */
    public static void commit() {
        execHandler(connection -> {
            try {
                TransactionContextUtil.doCommitTx(connection);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, "commit");
    }

    private static void execHandler(Consumer<Connection> consumer,String executorName)  {
        Map<String, Connection> connectionMap = CONNECTION.get();
        if (connectionMap == null || connectionMap.isEmpty()) {
            log.warn("Connection cannot be null");
            throw new IllegalArgumentException("Connection cannot be null");
        }
        try {
            for (Map.Entry<String, Connection> entry : connectionMap.entrySet()) {
                Connection connection = entry.getValue();
                if (connection != null) {
                    try {
                        consumer.accept(connection);
                    } catch (Exception e) {
                        log.error("Failed to {} transaction for connection: {} " ,executorName,entry.getKey(),e);
                        throw e;
                    }
                }
            }
        } finally {
            CONNECTION.remove();
        }
    }

    /**
     * 回滚事务内所有连接回滚
     */
    public static void rollback()  {
        execHandler(connection -> {
            try {
                TransactionContextUtil.doRollbackTx(connection);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, "rollback");
    }
}
