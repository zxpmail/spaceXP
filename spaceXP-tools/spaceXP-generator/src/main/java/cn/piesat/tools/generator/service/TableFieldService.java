package cn.piesat.tools.generator.service;


import cn.piesat.tools.generator.model.dto.TableFieldDTO;
import cn.piesat.tools.generator.model.entity.TableFieldDO;

import cn.piesat.tools.generator.model.vo.TableFieldVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

import java.util.Set;

/**
 * <p/>
 * {@code @description}  :表字段服务接口类
 * <p/>
 * <b>@create:</b> 2023/12/4 13:29.
 *
 * @author zhouxp
 */
public interface TableFieldService extends IService<TableFieldDO> {


     Boolean deleteByTableId(Long tableId);

     Boolean deleteByTableId(List<Long> tableId);

    Set<String> getPackageByTableId(Long id);

    List<TableFieldVO> getTableFieldsByTableId(Long id);

    Boolean update(List<TableFieldDTO> tableFields);
}
