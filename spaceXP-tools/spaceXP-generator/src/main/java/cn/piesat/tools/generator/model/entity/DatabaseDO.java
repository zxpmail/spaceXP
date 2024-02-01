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
     * 是否拼接数据库名称 0否 1是 由于ck数据库直接连接要增加表名 默认ck为 其他数据库为0
     */
    private Integer addDatabaseName;
    /**
     * 驱动类
     */
    private String driverClassName;

    /**
     * 表字段信息查询 SQL
     */
    private String tableFields;
    /**
     * 表信息查询SQL
     */
    private String tableSql;

    /**
     * url前缀
     */
    private String url;

}
