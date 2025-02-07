package cn.piesat.framework.dynamic.datasource.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p/>
 * {@code @description}: XA数据源
 * <p/>
 * {@code @create}: 2025-02-07 17:00
 * {@code @author}: zhouxp
 */
@Getter
@AllArgsConstructor
public enum XADataSourceEnum {
    MYSQL("com.mysql.cj.jdbc.MysqlXADataSource"),
    ORACLE("oracle.jdbc.xa.client.OracleXADataSource"),
    POSTGRESQL("org.postgresql.xa.PGXADataSource"),
    H2("org.h2.jdbcx.JdbcDataSource");
    private final String xaDriverClassName;
    private static final Map<String, XADataSourceEnum> sourceMap
            = Arrays.stream(values()).collect(Collectors.toMap(XADataSourceEnum::getXaDriverClassName, v -> v));

    public static boolean contains(String sourceValue) {
        return sourceMap.get(sourceValue) != null;
    }

}
