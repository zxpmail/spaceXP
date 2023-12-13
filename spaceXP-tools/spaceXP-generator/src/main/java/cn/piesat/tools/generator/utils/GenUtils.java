package cn.piesat.tools.generator.utils;

import cn.piesat.tools.generator.model.entity.DatabaseDO;
import cn.piesat.tools.generator.model.entity.TableDO;
import cn.piesat.tools.generator.model.entity.TableFieldDO;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * {@code @description}  :生成代码工具类
 * <p/>
 * <b>@create:</b> 2023/12/5 14:18.
 *
 * @author zhouxp
 */
@Slf4j
public class GenUtils {

    /**
     * 根据数据源，获取全部数据表
     *
     * @param connection 数据源
     */
    public static List<TableDO> getTableList(Connection connection, DatabaseDO databaseDO) {
        List<TableDO> tableList = new ArrayList<>();
        try {

            //查询数据
            PreparedStatement preparedStatement = connection.prepareStatement(databaseDO.tableSql(null));
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                TableDO table = new TableDO();
                table.setTableName(rs.getString(databaseDO.getTableName()));
                table.setTableComment(rs.getString(databaseDO.getTableComment()));
                table.setDatasourceId(databaseDO.getId());
                tableList.add(table);
            }

            connection.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return tableList;
    }

    public static List<TableFieldDO> getTableFieldList(Connection connection, DatabaseDO databaseDO, Long tableId, String tableName) {
        List<TableFieldDO> tableFieldList = new ArrayList<>();

        try {
            String tableFieldsSql = databaseDO.getTableFields();
            if ("Oracle".equalsIgnoreCase(databaseDO.getDbType())) {
                DatabaseMetaData md = connection.getMetaData();
                tableFieldsSql = String.format(tableFieldsSql.replace("#schema", md.getUserName()), tableName);
            } else {
                tableFieldsSql = String.format(tableFieldsSql, tableName);
            }
            PreparedStatement preparedStatement = connection.prepareStatement(tableFieldsSql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                TableFieldDO field = new TableFieldDO();
                field.setTableId(tableId);
                field.setFieldName(rs.getString(databaseDO.getFieldName()));
                String fieldType = rs.getString(databaseDO.getFieldType());
                if (fieldType.contains(" ")) {
                    fieldType = fieldType.substring(0, fieldType.indexOf(" "));
                }
                field.setFieldType(fieldType);
                field.setFieldComment(rs.getString(databaseDO.getFieldComment()));
                String key = rs.getString(databaseDO.getFieldKey());
                field.setPrimaryPk(StringUtils.isNotBlank(key) && "PRI".equalsIgnoreCase(key));

                tableFieldList.add(field);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return tableFieldList;

    }
}
