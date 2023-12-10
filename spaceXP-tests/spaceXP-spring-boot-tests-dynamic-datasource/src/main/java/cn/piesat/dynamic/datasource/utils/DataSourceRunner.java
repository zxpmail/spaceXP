package cn.piesat.dynamic.datasource.utils;

import cn.piesat.dynamic.datasource.dao.mapper.DbInfoMapper;
import cn.piesat.dynamic.datasource.model.entity.DbInfo;
import cn.piesat.framework.dynamic.datasource.core.DynamicDataSource;
import cn.piesat.framework.dynamic.datasource.model.DataSourceEntity;

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
 * {@code @description}  :
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
    private DbInfoMapper dbInfoMapper;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<DbInfo> dbInfos = dbInfoMapper.selectList(null);
        if (CollectionUtils.isNotEmpty(dbInfos)) {
            List<DataSourceEntity> ds = new ArrayList<>();
            for (DbInfo dbInfo : dbInfos) {
                DataSourceEntity sourceEntity = new DataSourceEntity();
                BeanUtils.copyProperties(dbInfo,sourceEntity);
                sourceEntity.setKey(dbInfo.getName());
                ds.add(sourceEntity);
            }
            dynamicDataSource.add(ds);
        }
    }
}

