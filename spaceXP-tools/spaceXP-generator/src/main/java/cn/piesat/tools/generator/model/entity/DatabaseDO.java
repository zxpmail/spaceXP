package cn.piesat.tools.generator.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * <p/>
 * {@code @description}  :数据库信息实体类
 * <p/>
 * <b>@create:</b> 2023/12/5 10:37.
 *
 * @author zhouxp
 */
@Data
@TableName("gen_database")
public class DatabaseDO extends BaseDO{

    /**
     * 数据库类型
     */
    private String dbType;
    /**
     * 驱动类
     */
    private String driver;
    /**
     * 表名称
     */
    private String tableName;
    /**
     * 表注释
     */
    private String tableComment;
    /**
     * 字段名称
     */
    private String fieldName;
    /**
     *  字段类型
     */
    private String fieldType;
    /**
     * 字段注释
     */
    private String fieldComment;
    /**
     * 主键字段
     */
    private String fieldKey;
    /**
     * 表字段信息查询 SQL
     */
    private String tableFields;
    /**
     * 表信息查询SQL
     */
    private String tableSql;
    /**
     * 表信息查询表名SQL
     */
    private String tableNameSql;
    /**
     * 表信息查询附加SQL
     */
    private String tableAddSql;
    /**
     * url前缀
     */
    private String url;



    public String tableSql(String tableName) {
        StringBuilder sql = new StringBuilder(getTableSql());
        // 表名查询
        if (StringUtils.hasText(tableName)) {
            sql.append(getTableNameSql()).append(tableName).append("' ");
        }
        if(StringUtils.hasText(getTableAddSql())){
            sql.append(getTableAddSql());
        }
        return sql.toString();
    }

}
