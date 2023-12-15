package cn.piesat.tools.generator.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * <p/>
 * {@code @description}  :数据源实体类
 * <p/>
 * <b>@create:</b> 2023/12/4 9:32.
 *
 * @author zhouxp
 */
@Data
@TableName("gen_datasource")
public class DataSourceDO extends BaseDO {

    /**
     * 数据库类型
     */
    private String dbType;

    /**
     * 数据库信息ID
     */
    private Long databaseId;

    /**
     * 驱动类
     */
    private String driverClassName;

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
