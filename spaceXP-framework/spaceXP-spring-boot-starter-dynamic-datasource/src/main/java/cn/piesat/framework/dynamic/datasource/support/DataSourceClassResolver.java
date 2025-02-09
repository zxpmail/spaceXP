package cn.piesat.framework.dynamic.datasource.support;

import java.lang.reflect.Method;

/**
 * <p/>
 * {@code @description}: 数据源解析器
 * <p/>
 * {@code @create}: 2025-02-09 18:17
 * {@code @author}: zhouxp
 */
public interface DataSourceClassResolver {
    /**
     * 查找数据源名称
     */
    String findKey(Method method, Object targetObject);
}
