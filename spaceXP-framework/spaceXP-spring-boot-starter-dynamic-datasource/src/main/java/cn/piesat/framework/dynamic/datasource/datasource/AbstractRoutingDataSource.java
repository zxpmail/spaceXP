package cn.piesat.framework.dynamic.datasource.datasource;

import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.jdbc.datasource.SmartDataSource;

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
public abstract class AbstractRoutingDataSource extends AbstractDataSource implements SmartDataSource {
    @Override
    public boolean shouldClose(Connection con) {
        return true;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return determineDataSource().getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return determineDataSource().getConnection(username,password);
    }

    /**
     * 通过子类来获取具体的数据源
     */
    protected abstract DataSource determineDataSource();
}
