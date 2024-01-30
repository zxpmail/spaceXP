package cn.piesat.tools.generator.service.impl;

import cn.piesat.framework.dynamic.datasource.core.DynamicDataSource;
import cn.piesat.tools.generator.model.dto.ImportDataSourceDTO;
import cn.piesat.tools.generator.model.entity.DataSourceDO;
import cn.piesat.tools.generator.model.entity.DatabaseDO;
import cn.piesat.tools.generator.model.entity.FieldTypeDO;
import cn.piesat.tools.generator.model.entity.ProjectDO;
import cn.piesat.tools.generator.model.entity.TableDO;
import cn.piesat.tools.generator.model.entity.TableFieldDO;
import cn.piesat.tools.generator.model.vo.TableVO;
import cn.piesat.tools.generator.service.DatabaseService;
import cn.piesat.tools.generator.service.ImportDataService;
import cn.piesat.tools.generator.utils.StrUtils;
import cn.piesat.tools.generator.utils.TemplateUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p/>
 * {@code @description}  :导入数据服务实现类
 * <p/>
 * <b>@create:</b> 2024/1/18 13:44.
 *
 * @author zhouxp
 */
@Service("importDataService")
@RequiredArgsConstructor
public class ImportDataServiceImpl implements ImportDataService {

    private final DynamicDataSource dynamicDataSource;
    private final DatabaseService databaseService;

    @Override
    public List<TableVO> getAllTablesByDataSource(ImportDataSourceDTO importDataSourceDTO) {
        DatabaseDO databaseDO = databaseService.getById(importDataSourceDTO.getDatabaseId());
        assert databaseDO != null;
        return getSqlByTable(databaseDO, importDataSourceDTO);
    }

    @Override
    public List<TableFieldDO> getALlFieldsByDataSourceAndTables(Map<String, FieldTypeDO> map, TableDO table, DatabaseDO databaseDO, DataSourceDO dataSourceDO) {
        String tableFieldsSql = databaseDO.getTableFields();
        DataSource dataSource = dynamicDataSource.getDataSource(dataSourceDO.getConnName());
        if ("Oracle".equalsIgnoreCase(databaseDO.getDbType())) {
            tableFieldsSql = String.format(tableFieldsSql.replace("#schema", dataSourceDO.getUsername()), table.getTableName());
        } else {
            tableFieldsSql = String.format(tableFieldsSql,  table.getTableName());
        }
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(tableFieldsSql, (rs, rowNum) -> {
            TableFieldDO f = new TableFieldDO();
            f.setTableId(table.getId());
            f.setFieldName(rs.getString(databaseDO.getFieldName()));
            f.setAttrName(StrUtils.underlineToCamel(f.getFieldName(),false));
            String fieldType = rs.getString(databaseDO.getFieldType());
            if (fieldType.contains(" ")) {
                fieldType = fieldType.substring(0, fieldType.indexOf(" "));
            }
            f.setFieldType(fieldType);
            f.setFieldComment(rs.getString(databaseDO.getFieldComment()));
            String key = rs.getString(databaseDO.getFieldKey());
            if(StringUtils.isNotBlank(key) && "PRI".equalsIgnoreCase(key)){
                f.setPrimaryPk(1);
            }else{
                f.setPrimaryPk(0);
            }
            // 获取字段对应的类型
            FieldTypeDO fieldTypeDO = map.get(f.getFieldType().toLowerCase());
            if (Objects.isNull(fieldTypeDO)) {
                // 没找到对应的类型，则为Object类型
                f.setAttrType("Object");
            } else {
                f.setAttrType(fieldTypeDO.getAttrType());
                f.setPackageName(fieldTypeDO.getPackageName());
            }
            f.setSort(rowNum);
            f.setAutoFill("DEFAULT");
            f.setFormItem(1);
            f.setGridItem(1);
            f.setQueryType("=");
            f.setQueryFormType("text");
            f.setFormType("text");
            f.setSortType(0);
            return f;
        });
    }

    private List<TableVO> getSqlByTable(DatabaseDO databaseDO, ImportDataSourceDTO importDataSourceDTO) {
        ProjectDO project = TemplateUtils.project;
        DataSource dataSource = dynamicDataSource.getDataSource(importDataSourceDTO.getConnName());
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(databaseDO.getTableSql(), (rs, rowNum) -> {
            TableVO table = new TableVO();
            table.setTableName(rs.getString(databaseDO.getTableName()));
            String comment =rs.getString(databaseDO.getTableComment());
            if(StringUtils.isEmpty(comment)){
                table.setTableComment(table.getTableName());
            }else {
                table.setTableComment(rs.getString(comment));
            }
            table.setDbType(importDataSourceDTO.getDbType());
            table.setDatasourceId(importDataSourceDTO.getId());
            table.setConnName(importDataSourceDTO.getConnName());
            table.setAuthor(project.getAuthor());
            table.setEmail(project.getEmail());
            table.setVersion(project.getVersion());
            table.setFunctionName(table.getTableName());
            table.setPackageName(project.getGroupId());
            table.setModuleName(project.getArtifactId());
            table.setClassName(StrUtils.underlineToCamel(table.getTableName(),true));
            table.setFormLayout(1);
            return table;
        });
    }
}
