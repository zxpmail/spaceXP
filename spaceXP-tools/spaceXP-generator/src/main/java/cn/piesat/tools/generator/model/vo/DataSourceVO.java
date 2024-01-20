package cn.piesat.tools.generator.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;


/**
 * <p/>
 * {@code @description}  :数据源VO实体类
 * <p/>
 * <b>@create:</b> 2023/12/4 15:36.
 *
 * @author zhouxp
 */
@Data
public class DataSourceVO {
    /**
     * id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 驱动类
     */
    private String driverClassName;

    /**
     * 数据库信息ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long databaseId;
    /**
     * 数据库类型
     */
    private String dbType;

    /**
     * 连接名
     */
    private String connName;
    /**
     * URL
     */
    private String url;
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

}
