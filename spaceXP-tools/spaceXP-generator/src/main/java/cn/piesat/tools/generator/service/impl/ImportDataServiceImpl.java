package cn.piesat.tools.generator.service.impl;

import cn.piesat.framework.dynamic.datasource.core.DynamicDataSource;
import cn.piesat.tools.generator.config.TemplatesConfig;
import cn.piesat.tools.generator.model.dto.ImportDataSourceDTO;
import cn.piesat.tools.generator.model.entity.DataSourceDO;
import cn.piesat.tools.generator.model.entity.DatabaseDO;
import cn.piesat.tools.generator.model.entity.FieldTypeDO;
import cn.piesat.tools.generator.model.entity.TableDO;
import cn.piesat.tools.generator.model.entity.TableFieldDO;
import cn.piesat.tools.generator.model.vo.TableVO;
import cn.piesat.tools.generator.service.DatabaseService;
import cn.piesat.tools.generator.service.ImportDataService;
import cn.piesat.tools.generator.utils.StrUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

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
@Slf4j
public class ImportDataServiceImpl implements ImportDataService {

    private final DynamicDataSource dynamicDataSource;
    private final DatabaseService databaseService;

    private final TemplatesConfig templatesConfig;

    @Override
    public List<TableVO> getAllTablesByDataSource(ImportDataSourceDTO importDataSourceDTO) {
        DatabaseDO databaseDO = databaseService.getById(importDataSourceDTO.getDatabaseId());
        assert databaseDO != null;
        return getSqlByTable(databaseDO, importDataSourceDTO);
    }

    /**
     * 删除代码中的注释、空白字符和换行符
     *
     * @param code 需要处理的代码字符串
     * @return 清理后的代码字符串
     */
    public static String cleanCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return "";
        }
        try {
            StringBuilder sb = new StringBuilder(code);

            sb = new StringBuilder(sb.toString().replaceAll("//.*?(\r\n|\r|\n|$)", ""));

            sb = new StringBuilder(sb.toString().replaceAll("/\\*[^*]*\\*+([^/*][^*]*\\*+)*/", ""));

            sb = new StringBuilder(sb.toString().replaceAll("\\s+", ""));

            return sb.toString();
        } catch (Exception e) {
            log.error("Error cleaning code:{} " , e.getMessage(),e);
            return code;
        }
    }
    @Override
    public List<TableFieldDO> getALlFieldsByDataSourceAndTables(Map<String, FieldTypeDO> map, TableDO table, DatabaseDO databaseDO, DataSourceDO dataSourceDO) {
        String tableFieldsSql = databaseDO.getTableFields();
        DataSource dataSource = dynamicDataSource.getDataSource(dataSourceDO.getConnName());
        if (databaseDO.getAddDatabaseName() == 1) {
            tableFieldsSql = String.format(tableFieldsSql, dataSourceDO.getDatabaseName(), table.getTableName());
        } else {
            tableFieldsSql = String.format(tableFieldsSql, table.getTableName());
        }

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        AtomicReference<Boolean> hasPK = new AtomicReference<>(false);
        List<TableFieldDO> query = jdbcTemplate.query(tableFieldsSql, (rs, rowNum) -> {
            TableFieldDO f = new TableFieldDO();
            f.setTableId(table.getId());
            f.setFieldName(rs.getString("column_name"));
            f.setAttrName(StrUtils.underlineToCamel(f.getFieldName(), false));
            String fieldType = rs.getString("data_type");
            if (fieldType.contains(" ")) {
                fieldType = fieldType.substring(0, fieldType.indexOf(" "));
            }
            if (fieldType.contains("(")) {
                fieldType = fieldType.substring(0, fieldType.indexOf("("));
            }

            f.setFieldType(fieldType);
            String comment = cleanCode(rs.getString("column_comment"));
            if (StringUtils.isNotEmpty(comment)) {
                f.setFieldComment(comment);
            } else {
                f.setFieldComment(f.getFieldName());
            }
            String key = rs.getString("column_key");
            if (StringUtils.isNotBlank(key) && "PRI".equalsIgnoreCase(key)&& ! hasPK.get()) {
                f.setPrimaryPk(1);
                f.setAutoFill("ASSIGN_ID");
                hasPK.set(true);
            } else {
                f.setPrimaryPk(0);
                f.setAutoFill("DEFAULT");
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
            int len = rs.getInt("len");
            if (len != 0 && f.getAttrType().equals("String")) {
                f.setLen(len / 2);
            } else {
                f.setLen(0);
            }
            f.setFormRequired(0);
            f.setSortType(0);
            f.setFieldRepeat(0);
            f.setQueryItem(1);
            f.setGridSort(0);
            f.setDto(1);
            f.setVo(1);
            f.setGridList(1);
            f.setSort(rowNum);

            f.setFormItem(1);
            f.setGridItem(1);
            f.setQueryType("=");
            f.setQueryFormType("text");
            f.setFormType("text");
            return f;
        });
        if (!hasPK.get()) {
            throw new RuntimeException(table.getTableName() + " 表没有主键，必须设置主键！");
        }
        return query;
    }

    private List<TableVO> getSqlByTable(DatabaseDO databaseDO, ImportDataSourceDTO importDataSourceDTO) {

        DataSource dataSource = dynamicDataSource.getDataSource(importDataSourceDTO.getConnName());
        String tableSql = databaseDO.getTableSql();
        if (databaseDO.getAddDatabaseName() == 1) {
            tableSql = String.format(tableSql, importDataSourceDTO.getDatabaseName());
        }

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(tableSql, (rs, rowNum) -> {
            TableVO table = new TableVO();
            table.setTableName(rs.getString("table_name"));
            String comment = rs.getString("table_comment");
            if (StringUtils.isEmpty(comment)) {
                table.setTableComment(table.getTableName());
            } else {
                table.setTableComment(comment);
            }
            table.setDbType(importDataSourceDTO.getDbType());
            table.setDatasourceId(importDataSourceDTO.getId());
            table.setConnName(importDataSourceDTO.getConnName());
            table.setAuthor(templatesConfig.getProject().getAuthor());
            table.setEmail(templatesConfig.getProject().getEmail());
            table.setVersion(templatesConfig.getProject().getVersion());
            table.setFunctionName(table.getTableName());
            table.setPackageName(templatesConfig.getProject().getGroupId());
            table.setModuleName(templatesConfig.getProject().getArtifactId());
            table.setClassName(table.getTableName());
            table.setFormLayout(1);
            return table;
        });
    }
}
