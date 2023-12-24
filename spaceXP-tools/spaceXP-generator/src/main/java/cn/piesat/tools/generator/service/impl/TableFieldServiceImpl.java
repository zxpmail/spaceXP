package cn.piesat.tools.generator.service.impl;

import cn.piesat.framework.dynamic.datasource.annotation.DS;
import cn.piesat.framework.dynamic.datasource.core.DynamicDataSource;
import cn.piesat.framework.dynamic.datasource.model.DSEntity;
import cn.piesat.tools.generator.mapper.TableFieldMapper;
import cn.piesat.tools.generator.model.entity.DatabaseDO;
import cn.piesat.tools.generator.model.entity.TableFieldDO;
import cn.piesat.tools.generator.service.TableFieldService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Functions;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    @Override
    public Map<String, TableFieldDO> getMap() {
        List<TableFieldDO> list = this.list();
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.stream().collect(Collectors.toMap(t -> t.getFieldName().toLowerCase(), Functions.identity(), (d1, d2) -> d1));
    }

    @SneakyThrows
    @DS
    @Override
    public List<TableFieldDO> getTableFieldList(String tableName,DatabaseDO databaseDO, DSEntity dsEntity) {
        String tableFieldsSql = databaseDO.getTableFields();
        if ("Oracle".equalsIgnoreCase(databaseDO.getDbType())) {
            DatabaseMetaData md = dynamicDataSource.getConnection().getMetaData();

            tableFieldsSql = String.format(tableFieldsSql.replace("#schema", md.getUserName()),tableName);
        } else {
            tableFieldsSql = String.format(tableFieldsSql, tableName);
        }
        return baseMapper.getFieldBySql(tableFieldsSql);

    }
}
