package cn.piesat.tools.generator.service;

import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;
import cn.piesat.tools.generator.model.entity.DataSourceDO;
import cn.piesat.tools.generator.model.entity.TableDO;
import cn.piesat.tools.generator.model.query.DataSourceQuery;
import cn.piesat.tools.generator.model.vo.DataSourceVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p/>
 * {@code @description}  :数据源服务接口类
 * <p/>
 * <b>@create:</b> 2023/12/4 13:29.
 *
 * @author zhouxp
 */
public interface DataSourceService extends IService<DataSourceDO> {

    PageResult list(PageBean pageBean, DataSourceQuery dataSourceQuery);

    DataSourceVO info(Long id);

    Boolean add(DataSourceVO dataSourceVO);

    Boolean update(DataSourceVO dataSourceVO);

    Boolean delete(List<Long> ids);

    Boolean delete(Long id);

    Boolean test(DataSourceVO dataSourceVO);


}
