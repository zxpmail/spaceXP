package cn.piesat.tools.generator.model.query;

import lombok.Data;

/**
 * <p/>
 * {@code @description}  :数据源查询实体类
 * <p/>
 * <b>@create:</b> 2023/12/4 14:51.
 *
 * @author zhouxp
 */
@Data
public class DataSourceQuery {
    /**
     * 连接名
     */
    private String connName;
    /**
     * 数据库类型
     */
    private String dbType;
}
