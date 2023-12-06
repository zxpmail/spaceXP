package cn.piesat.tools.generator.service;

import cn.piesat.tools.generator.model.entity.TableDO;
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
    void tableImport(Long datasourceId, List<TableVO> tableNameList);
}
