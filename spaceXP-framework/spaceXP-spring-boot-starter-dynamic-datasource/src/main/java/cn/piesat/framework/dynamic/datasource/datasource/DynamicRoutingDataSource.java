package cn.piesat.framework.dynamic.datasource.datasource;

import cn.piesat.framework.dynamic.datasource.constants.DataSourceConstant;
import cn.piesat.framework.dynamic.datasource.provider.DynamicDataSourceProvider;
import cn.piesat.framework.dynamic.datasource.utils.DynamicDataSourceContextHolder;
import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p/>
 * {@code @description}: 实现动态数据源路由类
 * <p/>
 * {@code @create}: 2025-02-09 14:16
 * {@code @author}: zhouxp
 */
@Slf4j
public class DynamicRoutingDataSource extends AbstractRoutingDataSource implements InitializingBean, DisposableBean {

    private final List<DynamicDataSourceProvider> providers;

    private String primary = DataSourceConstant.MASTER;

    private final Map<String, DataSource> allDataSources = new ConcurrentHashMap<>();

    public DynamicRoutingDataSource(List<DynamicDataSourceProvider> providers) {
        this.providers = providers;
    }

    @Override
    public void destroy() throws Exception {
        log.info("关闭所有数据源");
        for (Map.Entry<String, DataSource> entry : allDataSources.entrySet()) {
            closeDataSource(entry.getKey(), entry.getValue());
        }
    }

    public void close(String dsName){
        DataSource ds = allDataSources.remove(dsName);
        closeDataSource(dsName,ds);
    }
    private void closeDataSource(String ds, DataSource dataSource) {
        if (dataSource == null) {
            log.warn("数据源 {} 为空，无需关闭", ds);
            return;
        }
        try {
            if (dataSource instanceof DruidDataSource) {
                ((DruidDataSource) dataSource).close();
            } else if (dataSource instanceof HikariDataSource) {
                ((HikariDataSource) dataSource).close();
            }  else if (dataSource instanceof AutoCloseable) {
                ((AutoCloseable) dataSource).close();
            } else {
                Method close = ReflectionUtils.findMethod(dataSource.getClass(), "close");
                if (Objects.nonNull(close)) {
                    close.invoke(dataSource);
                }
            }
        } catch (Exception e) {
            log.error("关闭数据源 {} 失败: {}", ds, e.getMessage(), e);
            throw new RuntimeException("关闭数据源 " + ds + " 失败", e);
        }
    }

    public DataSource getDataSource(String dataSource) {
        if (StringUtils.hasText(dataSource)) {
            return allDataSources.get(dataSource);
        }
        return null;
    }

    private DataSource getDefaultDataSource() {
        DataSource dataSource = allDataSources.get(primary);
        if (dataSource != null) {
            return dataSource;
        }
        throw new RuntimeException("当前没有找到默认数据源");
    }

    @Override
    public void afterPropertiesSet() {
        Map<String, DataSource> dataSourceMap = new HashMap<>(16);
        providers.stream().map(DynamicDataSourceProvider::loadDataSources).forEach(dataSourceMap::putAll);
        for (Map.Entry<String, DataSource> entry : dataSourceMap.entrySet()) {
            addDataSource(entry.getKey(), entry.getValue());
        }
    }

    public void addDataSource(String ds, DataSource dataSource) {
        DataSource oldDataSource = allDataSources.put(ds, dataSource);
        if (oldDataSource != null) {
            closeDataSource(ds, oldDataSource);
        }
        log.info("添加数据源 {} 成功", ds);
    }


    public void setPrimary(String primary) {
        this.primary = primary;
    }


    @Override
    protected DataSource determineDataSource() {
        String currentDataSource = DynamicDataSourceContextHolder.getCurrentDataSource();

        if (!StringUtils.hasText(currentDataSource)) {
            log.warn("当前数据源为空！, 使用默认数据源: {}", primary);
            currentDataSource = primary;
        }
        DataSource dataSource = getDataSource(currentDataSource);
        if (dataSource == null) {
            dataSource = getDefaultDataSource();
            if(dataSource == null) {
                log.error("{}：数据源为空", currentDataSource);
                throw new RuntimeException(currentDataSource + ": 数据源为空");
            }
        }
        return dataSource;
    }

    @Override
    protected String getPrimary() {
        return primary;
    }
}
