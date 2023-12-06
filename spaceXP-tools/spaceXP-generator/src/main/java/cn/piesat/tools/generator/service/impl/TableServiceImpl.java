package cn.piesat.tools.generator.service.impl;

import cn.piesat.framework.common.utils.CopyBeanUtils;
import cn.piesat.tools.generator.mapper.TableMapper;
import cn.piesat.tools.generator.model.entity.DatabaseDO;
import cn.piesat.tools.generator.model.entity.TableDO;
import cn.piesat.tools.generator.model.entity.TableFieldDO;
import cn.piesat.tools.generator.model.vo.DataSourceVO;
import cn.piesat.tools.generator.model.vo.TableVO;
import cn.piesat.tools.generator.service.DataSourceService;
import cn.piesat.tools.generator.service.DatabaseService;
import cn.piesat.tools.generator.service.TableFieldService;
import cn.piesat.tools.generator.service.TableService;
import cn.piesat.tools.generator.utils.GenUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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
public class TableServiceImpl  extends ServiceImpl<TableMapper, TableDO> implements TableService {
    private final TableFieldService tableFieldService;
    private final DatabaseService databaseService;
    private final DataSourceService dataSourceService;

    private boolean repeat(Long datasourceId,String tableName){
        LambdaQueryWrapper<TableDO> wrapper =new LambdaQueryWrapper<>();
        wrapper.eq(TableDO::getDatasourceId,datasourceId);
        wrapper.eq(TableDO::getTableName,tableName);
        return count(wrapper) > 0;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void tableImport(Long datasourceId, List<TableVO> tableNameList) {
        DataSourceVO sourceVO = dataSourceService.info(datasourceId);
        DatabaseDO databaseDO = databaseService.getById(sourceVO.getDatabaseId());
        Connection connection =null;
        try {
            //connection = DbUtils.getConnection(sourceVO.getDbType(), databaseDO.getDriver(), sourceVO.getConnUrl(), sourceVO.getUsername(), sourceVO.);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("数据源配置错误，请检查数据源配置！");
        }
        for (TableVO vo: tableNameList){
            if(repeat(datasourceId,vo.getTableName())){
                continue;
            }
            TableDO tableDO = CopyBeanUtils.copy(vo, TableDO::new);
            save(tableDO);
            // 获取原生字段数据
            List<TableFieldDO> tableFieldList = GenUtils.getTableFieldList(connection,databaseDO, tableDO.getId(), tableDO.getTableName());

        }

        // 获取原生字段数据
        // 初始化字段数据
        //tableFieldService.initFieldList(tableFieldList);

        // 保存列数据
        //tableFieldList.forEach(tableFieldService::save);

        try {
            //释放数据源
            connection.close();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void initFieldList(List<TableFieldDO> tableFieldList) {
        // 字段类型、属性类型映射
        Map<String, TableFieldDO> fieldTypeMap = null; //TableFieldDO.getMap();
        int index = 0;
        for (TableFieldDO field : tableFieldList) {
            field.setAttrName(StringUtils.underlineToCamel(field.getFieldName()));
            // 获取字段对应的类型
            TableFieldDO fieldTypeMapping = fieldTypeMap.get(field.getFieldType().toLowerCase());
            if (fieldTypeMapping == null) {
                // 没找到对应的类型，则为Object类型
                field.setAttrType("Object");
            } else {
                field.setAttrType(fieldTypeMapping.getAttrType());
                field.setPackageName(fieldTypeMapping.getPackageName());
            }
            field.setSort(index++);
        }
    }
}
