package cn.piesat.tools.generator.service;

import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;
import cn.piesat.tools.generator.model.dto.TableDTO;
import cn.piesat.tools.generator.model.entity.TableDO;
import cn.piesat.tools.generator.model.query.TableQuery;
import cn.piesat.tools.generator.model.vo.TableVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p/>
 * {@code @description}  :表服务接口类
 * <p/>
 * <b>@create:</b> 2023/12/4 13:29.
 *
 * @author zhouxp
 */
public interface TableService extends IService<TableDO> {

    PageResult list(PageBean pageBean, TableQuery tableQuery);


    Boolean delete(List<Long> ids);

    Boolean delete(Long id);

    Boolean sync(TableDTO tableDTO);

    TableVO info(Long id);

    Boolean update(TableDTO tableDTO);

    Boolean add(List<TableDTO> tableList);
}
