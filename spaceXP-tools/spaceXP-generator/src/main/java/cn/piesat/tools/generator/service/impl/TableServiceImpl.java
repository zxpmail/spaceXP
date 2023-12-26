package cn.piesat.tools.generator.service.impl;

import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;
import cn.piesat.framework.common.utils.CopyBeanUtils;
import cn.piesat.framework.dynamic.datasource.annotation.DS;
import cn.piesat.framework.dynamic.datasource.model.DSEntity;
import cn.piesat.framework.mybatis.plus.utils.QueryUtils;
import cn.piesat.tools.generator.mapper.TableMapper;
import cn.piesat.tools.generator.model.entity.DatabaseDO;
import cn.piesat.tools.generator.model.entity.FieldTypeDO;
import cn.piesat.tools.generator.model.entity.ProjectDO;
import cn.piesat.tools.generator.model.entity.TableDO;
import cn.piesat.tools.generator.model.entity.TableFieldDO;
import cn.piesat.tools.generator.model.query.TableQuery;
import cn.piesat.tools.generator.model.vo.DataSourceVO;
import cn.piesat.tools.generator.model.vo.TableVO;
import cn.piesat.tools.generator.service.DataSourceService;
import cn.piesat.tools.generator.service.DatabaseService;
import cn.piesat.tools.generator.service.FieldTypeService;
import cn.piesat.tools.generator.service.TableFieldService;
import cn.piesat.tools.generator.service.TableService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.util.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
public class TableServiceImpl  extends ServiceImpl<TableMapper, TableDO> implements TableService {
    @Resource
    private  TableFieldService tableFieldService;

    @Resource
    private  DatabaseService databaseService;

    @Resource
    private  DataSourceService dataSourceService;

    @Resource
    private FieldTypeService fieldTypeService;



    private boolean repeat(Long datasourceId,String tableName){
        LambdaQueryWrapper<TableDO> wrapper =new LambdaQueryWrapper<>();
        wrapper.eq(TableDO::getDatasourceId,datasourceId);
        wrapper.eq(TableDO::getTableName,tableName);
        return count(wrapper) > 0;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void tableImport(Long datasourceId, List<TableVO> tableList) {
        DataSourceVO sourceVO = dataSourceService.info(datasourceId);
        DatabaseDO databaseDO = databaseService.getById(sourceVO.getDatabaseId());
        Map<String, FieldTypeDO> map = fieldTypeService.getMap();
        DSEntity dsEntity =new DSEntity();
        dsEntity.setDSName__(sourceVO.getConnName());
        for (TableVO tableVO:tableList){
            if(repeat(datasourceId,tableVO.getTableName())){
                continue;
            }
            TableDO tableDO = CopyBeanUtils.copy(tableVO, TableDO::new);
            if(Objects.isNull(tableDO)){
                continue;
            }
            tableDO.setDatasourceId(sourceVO.getId());
            save(tableDO);
            tableFieldService.importField(map,tableDO.getId(),tableVO.getTableName(),databaseDO, dsEntity);
        }
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
    @DS
    public List<TableDO> getSqlByTable(String sql, DSEntity dsEntity) {
        return baseMapper.getTableBySql(sql);
    }

    private LambdaQueryWrapper<TableDO> getWrapper(TableQuery tableQuery) {
        LambdaQueryWrapper<TableDO> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StringUtils.hasText(tableQuery.getTableName()),TableDO::getTableName,tableQuery.getTableName());
        return wrapper;
    }
}
