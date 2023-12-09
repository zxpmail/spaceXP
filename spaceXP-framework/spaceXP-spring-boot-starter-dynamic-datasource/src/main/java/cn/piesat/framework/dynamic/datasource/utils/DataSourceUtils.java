package cn.piesat.framework.dynamic.datasource.utils;

import cn.piesat.framework.common.exception.BaseException;
import cn.piesat.framework.common.model.enums.CommonResponseEnum;
import cn.piesat.framework.dynamic.datasource.model.DataSourceEntity;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * <p/>
 * {@code @description}  : 数据源工具
 * <p/>
 * <b>@create:</b> 2023/12/9 15:06.
 *
 * @author zhouxp
 */
public class DataSourceUtils {
    /**
     * 测试数据源
     *
     * @param dataSourceEntity 数据源实体
     */
    public static DataSource test(DataSourceEntity dataSourceEntity) {
        DataSource dataSource = DataSourceBuilder.create()
                .url(dataSourceEntity.getUrl())
                .password(dataSourceEntity.getPassword())
                .username(dataSourceEntity.getUsername())
                .driverClassName(dataSourceEntity.getDriverClassName())
                .build();
        try {
            dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new BaseException(CommonResponseEnum.DATASOURCE_ERROR);
        }
        return dataSource;
    }
}
