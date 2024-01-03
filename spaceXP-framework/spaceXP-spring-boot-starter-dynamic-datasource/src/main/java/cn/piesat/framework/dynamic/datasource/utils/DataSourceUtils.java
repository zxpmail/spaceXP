package cn.piesat.framework.dynamic.datasource.utils;

import cn.piesat.framework.common.exception.BaseException;
import cn.piesat.framework.common.model.enums.CommonResponseEnum;
import cn.piesat.framework.dynamic.datasource.model.DSEntity;
import cn.piesat.framework.dynamic.datasource.model.DataSourceEntity;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
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

    public static String getDsName(ProceedingJoinPoint point){
        Object[] args = point.getArgs();
        String dsName="";
        if(args!=null){
            for (Object arg : args) {
                if(arg instanceof DSEntity){
                    dsName=((DSEntity) arg).getDSName__();
                    DataSourceContextHolder.push(dsName);
                    return dsName;
                }
            }
        }
        return null;
    }

    public static String getDsName(MethodInvocation point){
        Object[] args = point.getArguments();
        String dsName="";
        for (Object arg : args) {
            if(arg instanceof DSEntity){
                dsName=((DSEntity) arg).getDSName__();
                DataSourceContextHolder.push(dsName);
                return dsName;
            }
        }
        return null;
    }
}
