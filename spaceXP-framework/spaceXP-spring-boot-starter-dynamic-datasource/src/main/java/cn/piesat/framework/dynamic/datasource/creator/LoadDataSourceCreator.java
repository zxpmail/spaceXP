package cn.piesat.framework.dynamic.datasource.creator;

import cn.piesat.framework.dynamic.datasource.properties.DataSourceProperty;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.List;

/**
 * <p/>
 * {@code @description}: 加载数据源创建器
 * <p/>
 * {@code @create}: 2025-02-09 13:44
 * {@code @author}: zhouxp
 */
public class LoadDataSourceCreator {
    private final List<DataSourceCreator> dataSourceCreators;

    public LoadDataSourceCreator(List<DataSourceCreator> dataSourceCreators) {
        this.dataSourceCreators = dataSourceCreators;
    }

    public DataSource createDataSource(DataSourceProperty dataSourceProperty) {
        if (CollectionUtils.isEmpty(dataSourceCreators)) {
            throw new RuntimeException("当前没有创建数据源的执行器");
        }
        for (DataSourceCreator creator : dataSourceCreators) {
            if (creator.support(dataSourceProperty)) {
                return creator.createDataSource(dataSourceProperty);
            }
        }
        throw new RuntimeException(String.format("当前没有合适 %s 的创建数据源的执行器", dataSourceProperty.getType()));
    }
}
