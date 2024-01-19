package cn.piesat.tools.generator.service;

import cn.piesat.tools.generator.model.dto.ImportDataSourceDTO;
import cn.piesat.tools.generator.model.entity.DataSourceDO;
import cn.piesat.tools.generator.model.entity.DatabaseDO;
import cn.piesat.tools.generator.model.entity.FieldTypeDO;
import cn.piesat.tools.generator.model.entity.TableDO;
import cn.piesat.tools.generator.model.entity.TableFieldDO;
import cn.piesat.tools.generator.model.vo.TableVO;

import java.util.List;
import java.util.Map;

/**
 * <p/>
 * {@code @description}  :导入数据服务接口
 * <p/>
 * <b>@create:</b> 2024/1/18 13:43.
 *
 * @author zhouxp
 */
public interface ImportDataService {
    List<TableVO> getAllTablesByDataSource(ImportDataSourceDTO importDataSourceDTO);

    List<TableFieldDO> getALlFieldsByDataSourceAndTables(Map<String, FieldTypeDO> map, TableDO table, DatabaseDO databaseDO, DataSourceDO dataSourceDO);
}
