package cn.piesat.framework.dynamic.datasource.datasource;


import cn.piesat.framework.dynamic.datasource.tx.ConnectionFactory;
import cn.piesat.framework.dynamic.datasource.tx.ConnectionProxy;
import cn.piesat.framework.dynamic.datasource.tx.TransactionContext;
import cn.piesat.framework.dynamic.datasource.utils.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.jdbc.datasource.SmartDataSource;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * <p/>
 * {@code @description}:抽象数据源路由类
 * <p/>
 * {@code @create}: 2025-02-10 13:21
 * {@code @author}: zhouxp
 */
@Slf4j
public abstract class AbstractRoutingDataSource extends AbstractDataSource /*implements SmartDataSource*/ {
    @Override
    public Connection getConnection() throws SQLException {
        String xid = TransactionContext.getXID();
        if (!StringUtils.hasText(xid)) {
            return determineDataSource().getConnection();
        } else {
            String ds = DynamicDataSourceContextHolder.getCurrentDataSource();
            ds = !StringUtils.hasText(xid) ? getPrimary() : ds;
            ConnectionProxy connection = ConnectionFactory.getConnection(xid, ds);
            return connection == null ? getConnectionProxy(xid, ds, determineDataSource().getConnection()) : connection;
        }
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        String xid = TransactionContext.getXID();
        if (!StringUtils.hasText(xid)) {
            return determineDataSource().getConnection(username, password);
        } else {
            String ds = DynamicDataSourceContextHolder.getCurrentDataSource();
            ds = !StringUtils.hasText(xid) ? getPrimary() : ds;
            ConnectionProxy connection = ConnectionFactory.getConnection(xid, ds);
            return connection == null ? getConnectionProxy(xid, ds, determineDataSource().getConnection(username, password))
                    : connection;
        }
    }

    private Connection getConnectionProxy(String xid, String ds, Connection connection) {
        ConnectionProxy connectionProxy = new ConnectionProxy(connection, ds);
        ConnectionFactory.putConnection(xid, ds, connectionProxy);
        return connectionProxy;
    }

//    @Override
//    public boolean shouldClose(Connection con) {
//        if (StringUtils.hasText(txGlobalId)) {
//            //获取当前数据源的连接
//            String currentDataSource = DynamicDataSourceContextHolder.getCurrentDataSource();
//            Connection connection = TransactionConnectionContextHolder.getConnection(currentDataSource);
//            if (connection == con) {
//                return false;
//            }
//        }
//        return true;
//    }
    /**
     * 通过子类来获取具体的数据源
     */
    protected abstract DataSource determineDataSource();
    protected abstract String getPrimary();
}
