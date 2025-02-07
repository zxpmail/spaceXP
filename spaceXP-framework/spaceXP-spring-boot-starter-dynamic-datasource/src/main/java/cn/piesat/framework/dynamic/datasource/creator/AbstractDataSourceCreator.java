package cn.piesat.framework.dynamic.datasource.creator;

import cn.piesat.framework.dynamic.datasource.init.DataSourceInit;
import cn.piesat.framework.dynamic.datasource.properties.DataSourceProperty;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p/>
 * {@code @description}: AbstractDataSourceCreator
 * <p/>
 * {@code @create}: 2025-02-07 13:06
 * {@code @author}: zhouxp
 */
public abstract class AbstractDataSourceCreator implements DataSourceCreator {
    @Setter(onMethod_ = @Autowired)
    private List<DataSourceInit> dataSourceInits;

    @Override
    public DataSource createDataSource(DataSourceProperty dataSourceProperty) {
        DataSource dataSource;
        if (dataSourceInits != null && !dataSourceInits.isEmpty()) {
            List<DataSourceInit> sortedInits = dataSourceInits.stream()
                    .sorted(Comparator.comparing(DataSourceInit::getOrder))
                    .collect(Collectors.toList());
            synchronized (this) {
                sortedInits.forEach(init -> init.beforeCreate(dataSourceProperty));
                dataSource = doCreateDataSource(dataSourceProperty);
                sortedInits.forEach(init -> init.afterCreate(dataSourceProperty));
            }
        } else {
            dataSource = doCreateDataSource(dataSourceProperty);
        }
        return dataSource;
    }

    protected abstract DataSource doCreateDataSource(DataSourceProperty dataSourceProperty);
}
