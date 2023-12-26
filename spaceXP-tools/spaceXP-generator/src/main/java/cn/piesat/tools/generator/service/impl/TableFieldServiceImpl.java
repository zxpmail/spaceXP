package cn.piesat.tools.generator.service.impl;

import cn.piesat.framework.dynamic.datasource.annotation.DS;
import cn.piesat.framework.dynamic.datasource.core.DynamicDataSource;
import cn.piesat.framework.dynamic.datasource.model.DSEntity;
import cn.piesat.tools.generator.mapper.TableFieldMapper;
import cn.piesat.tools.generator.model.entity.DatabaseDO;
import cn.piesat.tools.generator.model.entity.FieldTypeDO;
import cn.piesat.tools.generator.model.entity.TableFieldDO;
import cn.piesat.tools.generator.service.TableFieldService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Functions;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.sql.DatabaseMetaData;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p/>
 * {@code @description}  :表字段接口实现类
 * <p/>
 * <b>@create:</b> 2023/12/4 13:31.
 *
 * @author zhouxp
 */
@Service
public class TableFieldServiceImpl extends ServiceImpl<TableFieldMapper, TableFieldDO> implements TableFieldService {

    @Resource
    private DynamicDataSource dynamicDataSource;

    @SneakyThrows
    @DS
    @Override
    public void importField(Map<String, FieldTypeDO> map, Long tableId, String tableName, DatabaseDO databaseDO, DSEntity dsEntity) {
        String tableFieldsSql = databaseDO.getTableFields();
        if ("Oracle".equalsIgnoreCase(databaseDO.getDbType())) {
            DatabaseMetaData md = dynamicDataSource.getConnection().getMetaData();

            tableFieldsSql = String.format(tableFieldsSql.replace("#schema", md.getUserName()), tableName);
        } else {
            tableFieldsSql = String.format(tableFieldsSql, tableName);
        }
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dynamicDataSource);
        List<TableFieldDO> query = jdbcTemplate.query(tableFieldsSql, (rs, rowNum) -> {
            TableFieldDO f = new TableFieldDO();
            f.setTableId(tableId);
            f.setFieldName(rs.getString(databaseDO.getFieldName()));
            f.setAttrName(StringUtils.underlineToCamel(f.getFieldName()));
            String fieldType = rs.getString(databaseDO.getFieldType());
            if (fieldType.contains(" ")) {
                fieldType = fieldType.substring(0, fieldType.indexOf(" "));
            }
            f.setFieldType(fieldType);
            f.setFieldComment(rs.getString(databaseDO.getFieldComment()));
            String key = rs.getString(databaseDO.getFieldKey());
            f.setPrimaryPk(StringUtils.isNotBlank(key) && "PRI".equalsIgnoreCase(key));
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
            return f;
        });
        this.saveBatch(query);
    }

    @Override
    public Boolean deleteByTableId(Long tableId) {
        LambdaQueryWrapper<TableFieldDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TableFieldDO::getTableId,tableId);
        return remove(wrapper);
    }

    @Override
    public Boolean deleteByTableId(List<Long> tableId) {
        LambdaQueryWrapper<TableFieldDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(TableFieldDO::getTableId,tableId);
        return remove(wrapper);
    }
}
