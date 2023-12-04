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
public class DataSourceDO {
    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 数据库类型
     */
    private String dbType;
    /**
     * ip地址
     */
    private String ipAddr;
    /**
     * 端口
     */
    private Integer port;
    /**
     * 数据库名称
     */
    private String databaseName;
    /**
     * 连接名
     */
    private String connName;
    /**
     * URL
     */
    private String connUrl;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 创建时间
     */
    private Date createTime;

}
