package cn.piesat.tools.generator.service.impl;

import cn.piesat.framework.dynamic.datasource.core.DynamicDataSource;
import cn.piesat.framework.dynamic.datasource.model.DSEntity;
import cn.piesat.tools.generator.model.GeneratorInfo;
import cn.piesat.tools.generator.model.dto.ImportDataSourceDTO;
import cn.piesat.tools.generator.model.entity.DataSourceDO;
import cn.piesat.tools.generator.model.entity.DatabaseDO;
import cn.piesat.tools.generator.model.entity.TableDO;
import cn.piesat.tools.generator.model.vo.ProjectVO;
import cn.piesat.tools.generator.model.vo.TableVO;
import cn.piesat.tools.generator.service.DatabaseService;
import cn.piesat.tools.generator.service.ImportDataService;
import cn.piesat.tools.generator.utils.ConfigUtils;
import cn.piesat.tools.generator.utils.StrUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

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

    private List<TableVO> getSqlByTable(DatabaseDO databaseDO, ImportDataSourceDTO importDataSourceDTO) {
        ProjectVO project = ConfigUtils.getGeneratorInfo().getProject();
        DataSource dataSource = dynamicDataSource.getDataSource(importDataSourceDTO.getConnName());
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(databaseDO.getTableSql(), (rs, rowNum) -> {
            TableVO table = new TableVO();
            table.setTableName(rs.getString(databaseDO.getTableName()));
            table.setTableComment(rs.getString(databaseDO.getTableComment()));
            table.setDatasourceId(importDataSourceDTO.getId());
            table.setConnName(importDataSourceDTO.getConnName());
            table.setAuthor(project.getAuthor());
            table.setEmail(project.getEmail());
            table.setVersion(project.getVersion());
            table.setFunctionName(table.getTableName());
            table.setModuleName(project.getGroupId());
            table.setModuleName(project.getArtifactId());
            table.setClassName(StrUtils.underlineToCamel(table.getTableName(),true));
            table.setFormLayout(1);
            return table;
        });
    }
}
