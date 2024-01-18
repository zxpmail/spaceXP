package cn.piesat.tools.generator.service;

import cn.piesat.tools.generator.model.dto.ImportDataSourceDTO;
import cn.piesat.tools.generator.model.vo.TableVO;

import java.util.List;

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
}
