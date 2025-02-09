package cn.piesat.framework.dynamic.datasource.provider;

import javax.sql.DataSource;
import java.util.Map;

/**
 * <p/>
 * {@code @description}: 多数据源加载接口
 * <p/>
 * {@code @create}: 2025-02-09 13:53
 * {@code @author}: zhouxp
 */
public interface DynamicDataSourceProvider {
    Map<String, DataSource> loadDataSources();
}
