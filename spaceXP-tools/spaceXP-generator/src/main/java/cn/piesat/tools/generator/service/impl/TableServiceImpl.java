package cn.piesat.tools.generator.service.impl;

import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;
import cn.piesat.framework.common.utils.CopyBeanUtils;
import cn.piesat.framework.mybatis.plus.utils.QueryUtils;
import cn.piesat.tools.generator.mapper.TableMapper;
import cn.piesat.tools.generator.model.dto.TableDTO;
import cn.piesat.tools.generator.model.entity.DataSourceDO;
import cn.piesat.tools.generator.model.entity.DatabaseDO;
import cn.piesat.tools.generator.model.entity.FieldTypeDO;
import cn.piesat.tools.generator.model.entity.TableDO;
import cn.piesat.tools.generator.model.entity.TableFieldDO;
import cn.piesat.tools.generator.model.query.TableQuery;
import cn.piesat.tools.generator.model.vo.TableVO;
import cn.piesat.tools.generator.service.DataSourceService;
import cn.piesat.tools.generator.service.DatabaseService;
import cn.piesat.tools.generator.service.FieldTypeService;
import cn.piesat.tools.generator.service.ImportDataService;
import cn.piesat.tools.generator.service.TableFieldService;
import cn.piesat.tools.generator.service.TableService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p/>
 * {@code @description}  :表接口实现类
 * <p/>
 * <b>@create:</b> 2023/12/4 13:31.
 *
 * @author zhouxp
 */
@Service
@RequiredArgsConstructor
public class TableServiceImpl extends ServiceImpl<TableMapper, TableDO> implements TableService {
    private final TableFieldService tableFieldService;

    private final DatabaseService databaseService;

    private final DataSourceService dataSourceService;


    private final FieldTypeService fieldTypeService;


    private boolean repeat(Long datasourceId, String tableName) {
        LambdaQueryWrapper<TableDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TableDO::getDatasourceId, datasourceId);
        wrapper.eq(TableDO::getTableName, tableName);
        return count(wrapper) > 0;
    }



    @Override
    public PageResult list(PageBean pageBean, TableQuery tableQuery) {
        IPage<TableDO> page = this.page(
                QueryUtils.getPage(pageBean),
                getWrapper(tableQuery)
        );
        return new PageResult(page.getTotal(), CopyBeanUtils.copy(page.getRecords(), TableVO::new));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(List<Long> ids) {
        tableFieldService.deleteByTableId(ids);
        return removeBatchByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Long id) {
        tableFieldService.deleteByTableId(id);
        return removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean sync(TableDTO tableDTO) {
        tableFieldService.deleteByTableId(tableDTO.getId());
        removeById(tableDTO.getId());
        add(new ArrayList<TableDTO>() {{
            add(tableDTO);
        }});
        return true;
    }

    @Override
    public TableVO info(Long id) {
        TableDO byId = getById(id);
        assert byId != null;
        TableVO copy = CopyBeanUtils.copy(byId, TableVO::new);
        assert copy != null;
        copy.setTableFields(tableFieldService.getTableFieldsByTableId(copy.getId()));
        return copy;
    }

    @Override
    public Boolean update(TableDTO tableDTO) {
        TableDO byId = getById(tableDTO.getId());
        if (Objects.isNull(byId)) {
            return false;
        } else {
            TableDO tableDO = CopyBeanUtils.copy(tableDTO, TableDO::new);
            return updateById(tableDO);
        }
    }

    private final ImportDataService importDataService;
    @Override
    public Boolean add(List<TableDTO> tableList) {
        if(CollectionUtils.isEmpty(tableList)){
            return false;
        }
        Long datasourceId = tableList.get(0).getDatasourceId();
        DataSourceDO dataSourceDO = dataSourceService.getById(datasourceId);
        DatabaseDO databaseDO = databaseService.getById(dataSourceDO.getDatabaseId());
        Map<String, FieldTypeDO> map = fieldTypeService.getMap();
        for (TableDTO table : tableList) {
            if (repeat(datasourceId, table.getTableName())) {
                continue;
            }
            TableDO tableDO = CopyBeanUtils.copy(table, TableDO::new);
            if (tableDO != null) {
                tableDO.setId(null);
            }else {
                continue;
            }
            save(tableDO);
            List<TableFieldDO> aLlFieldsByDataSourceAndTables = importDataService.getALlFieldsByDataSourceAndTables(map, tableDO, databaseDO, dataSourceDO);
            tableFieldService.saveBatch(aLlFieldsByDataSourceAndTables);
        }
        return true;
    }

    private LambdaQueryWrapper<TableDO> getWrapper(TableQuery tableQuery) {
        LambdaQueryWrapper<TableDO> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StringUtils.hasText(tableQuery.getTableName()), TableDO::getTableName, tableQuery.getTableName());
        return wrapper;
    }

}
