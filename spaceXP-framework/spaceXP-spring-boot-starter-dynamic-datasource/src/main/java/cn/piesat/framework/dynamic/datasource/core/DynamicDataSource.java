package cn.piesat.framework.dynamic.datasource.core;

import cn.piesat.framework.dynamic.datasource.init.DataSourceInit;
import cn.piesat.framework.dynamic.datasource.model.DataSourceEntity;
import cn.piesat.framework.dynamic.datasource.utils.DataSourceContextHolder;
import cn.piesat.framework.dynamic.datasource.utils.DataSourceUtils;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p/>
 * {@code @description}  : 实现动态数据源，根据AbstractRoutingDataSource路由到不同数据源中
 * <p/>
 * <b>@create:</b> 2023/12/9 11:11.
 *
 * @author zhouxp
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    private final Map<Object, Object> targetDataSourceMap;

    @Setter(onMethod_ = @Autowired)
    private List<DataSourceInit> dataSourceInits;

    public DynamicDataSource(DataSource defaultDataSource, Map<Object, Object> targetDataSources) {
        super.setDefaultTargetDataSource(defaultDataSource);
        super.setTargetDataSources(targetDataSources);
        this.targetDataSourceMap = targetDataSources;
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.peek();
    }

    /**
     * 测试数据源
     *
     * @param dataSourceEntity 数据源实体
     */
    public DataSource test(DataSourceEntity dataSourceEntity) {
        List<DataSourceInit> sortedInits = Optional.ofNullable(dataSourceInits)
                .orElse(Collections.emptyList())
                .stream()
                .sorted(Comparator.comparing(DataSourceInit::getOrder))
                .collect(Collectors.toList());
        sortedInits.forEach(init -> init.beforeCreate(dataSourceEntity));
        final DataSource[] dataSource = {DataSourceUtils.test(dataSourceEntity)};
        sortedInits.forEach(init -> dataSource[0] = init.afterCreate(dataSource[0], dataSourceEntity));
        return dataSource[0];
    }

    public DataSource getDataSource(String key) {
        if (targetDataSourceMap == null) {
           throw new RuntimeException("数据源集合为空");
        }
        Object o = targetDataSourceMap.get(key);
        if (o instanceof DataSource) {
            return (DataSource) o;
        } else {
            throw new RuntimeException("没有数据源！");
        }
    }

    /**
     * 增加数据源
     *
     * @param dataSourceEntity 数据源实体
     * @return 返回结果，true：增加成功，false：存在
     */
    public Boolean add(DataSourceEntity dataSourceEntity) {
        DataSource test = test(dataSourceEntity);
        if (!existsDataSource(dataSourceEntity.getKey())) {
            targetDataSourceMap.put(dataSourceEntity.getKey(), test);
            super.afterPropertiesSet();
            return true;
        }
        return false;
    }

    /**
     * 删除数据源
     *
     * @param key 数据源保存的key
     * @return 返回结果，true：增加成功，false：存在
     */
    public Boolean delete(String key) {
        if (existsDataSource(key)) {
            targetDataSourceMap.remove(key);
            super.afterPropertiesSet();
            return true;
        }
        return false;
    }

    /**
     * 增加数据源
     *
     * @param dataSource 数据源
     * @param key        数据源保存的key
     * @return 返回结果，true：增加成功，false：存在
     */
    public Boolean add(DataSource dataSource, String key) {
        if (!existsDataSource(key)) {
            targetDataSourceMap.put(key, dataSource);
            super.afterPropertiesSet();
            return true;
        }
        return false;
    }

    /**
     * 添加数据源信息
     *
     * @param dataSources 数据源实体集合
     */
    public void add(List<DataSourceEntity> dataSources) {
        for (DataSourceEntity dataSource : dataSources) {
            try {
                add(dataSource);
            } catch (Exception ignored) {

            }
        }

    }

    /**
     * 校验数据源是否存在
     *
     * @param key 数据源保存的key
     * @return 返回结果，true：存在，false：不存在
     */
    private Boolean existsDataSource(String key) {
        return Objects.nonNull(this.targetDataSourceMap.get(key));
    }
}
