package cn.piesat.tools.generator.utils;


import cn.piesat.framework.dynamic.datasource.core.DynamicDataSource;
import cn.piesat.framework.dynamic.datasource.model.DataSourceEntity;
import cn.piesat.tools.generator.model.entity.DataSourceDO;
import cn.piesat.tools.generator.service.DataSourceService;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * {@code @description}  :  启动时把数据库中的数据源加入内存中
 * <p/>
 * <b>@create:</b> 2023/12/9 19:58.
 *
 * @author zhouxp
 */
@Component
public class DataSourceRunner implements ApplicationRunner {
    @Resource
    private DynamicDataSource dynamicDataSource;
    @Resource
    private DataSourceService dataSourceService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<DataSourceDO> list = dataSourceService.list();

        if (CollectionUtils.isNotEmpty(list)) {
            List<DataSourceEntity> ds = new ArrayList<>();
            for (DataSourceDO dataSourceDO : list) {
                DataSourceEntity sourceEntity = new DataSourceEntity();
                BeanUtils.copyProperties(dataSourceDO,sourceEntity);
                sourceEntity.setKey(dataSourceDO.getConnName());
                ds.add(sourceEntity);
            }
            dynamicDataSource.add(ds);
        }
    }
}

