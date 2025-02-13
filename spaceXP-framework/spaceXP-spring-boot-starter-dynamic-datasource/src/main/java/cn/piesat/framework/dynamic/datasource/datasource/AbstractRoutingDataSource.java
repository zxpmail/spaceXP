package cn.piesat.framework.dynamic.datasource.datasource;

import cn.piesat.framework.dynamic.datasource.transaction.TransactionConnectionContextHolder;
import cn.piesat.framework.dynamic.datasource.transaction.TransactionContextUtil;
import cn.piesat.framework.dynamic.datasource.transaction.TransactionGlobalIdContextHolder;
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
public abstract class AbstractRoutingDataSource extends AbstractDataSource implements SmartDataSource {
    @Override
    public boolean shouldClose(Connection con) {
        return true;
    }


    @Override
    public Connection getConnection() throws SQLException {
        return doGetConnection(null,null);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return doGetConnection(username,password);
    }

    /**
     * 当没有事务直接返回连接，有事务进行事务处理
     */
    private Connection doGetConnection(String username, String password) throws SQLException {
        String txGlobalId = TransactionGlobalIdContextHolder.getTxGlobalId();
        if (!StringUtils.hasText(txGlobalId)) {
            if(StringUtils.hasText(username)&& StringUtils.hasText(password)){
                return determineDataSource().getConnection(username, password);
            }else{
                return determineDataSource().getConnection();
            }
        } else {
            String currentDataSource = DynamicDataSourceContextHolder.getCurrentDataSource();
            Connection connection = TransactionConnectionContextHolder.getConnection(currentDataSource);
            if (connection == null) {
                try {
                    if(StringUtils.hasText(username)&& StringUtils.hasText(password)){
                        connection = determineDataSource().getConnection(username, password);
                    }else{
                        connection = determineDataSource().getConnection();
                    }
                    Boolean putFirst = TransactionConnectionContextHolder.putConnection(currentDataSource, connection);
                    if (putFirst) {
                        TransactionContextUtil.doStartTx(connection);
                    }
                } catch (SQLException e) {
                    log.error("Failed to get connection", e);
                    throw e;
                }
            }
            return connection;
        }
    }

    /**
     * 通过子类来获取具体的数据源
     */
    protected abstract DataSource determineDataSource();
}
