package cn.piesat.tools.generator.service.impl;

import cn.piesat.tools.generator.mapper.DataSourceMapper;
import cn.piesat.tools.generator.model.entity.DataSourceDO;
import cn.piesat.tools.generator.service.DataSourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p/>
 * {@code @description}  :数据源实现类
 * <p/>
 * <b>@create:</b> 2023/12/4 13:31.
 *
 * @author zhouxp
 */
@Service
public class DataSourceServiceImpl extends ServiceImpl<DataSourceMapper, DataSourceDO> implements DataSourceService {
}
