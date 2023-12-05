package cn.piesat.tools.generator.utils;

import com.baomidou.mybatisplus.annotation.DbType;
import oracle.jdbc.driver.OracleConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * <p/>
 * {@code @description}  :数据库工具类
 * <p/>
 * <b>@create:</b> 2023/12/5 11:22.
 *
 * @author zhouxp
 */
public class DbUtils {
    private static final int CONNECTION_TIMEOUTS_SECONDS = 6;

    /**
     * 获得数据库连接
     */
    public static Connection getConnection(String dbType,String driverClass,String connUrl,String username,String password) throws ClassNotFoundException, SQLException {
        DriverManager.setLoginTimeout(CONNECTION_TIMEOUTS_SECONDS);
        Class.forName(driverClass);

        Connection connection = DriverManager.getConnection(connUrl, username, password);
        if ("Oracle".equalsIgnoreCase(dbType)) {
            ((OracleConnection) connection).setRemarksReporting(true);
        }

        return connection;
    }
}
