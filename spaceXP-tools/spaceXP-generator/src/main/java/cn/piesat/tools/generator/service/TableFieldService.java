package cn.piesat.tools.generator.service;

import cn.piesat.framework.dynamic.datasource.model.DSEntity;
import cn.piesat.tools.generator.model.entity.DatabaseDO;
import cn.piesat.tools.generator.model.entity.FieldTypeDO;
import cn.piesat.tools.generator.model.entity.TableFieldDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p/>
 * {@code @description}  :表字段服务接口类
 * <p/>
 * <b>@create:</b> 2023/12/4 13:29.
 *
 * @author zhouxp
 */
public interface TableFieldService extends IService<TableFieldDO> {

     void  importField(Map<String, FieldTypeDO> map, Long tableId, String tableName, DatabaseDO databaseDO, DSEntity dsEntity);

}
