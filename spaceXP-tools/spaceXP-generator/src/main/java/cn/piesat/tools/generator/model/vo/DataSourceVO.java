package cn.piesat.tools.generator.model.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

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
    @TableId
    private Long id;

    /**
     * 是否是当前连接
     */
    private Integer isCurrent;
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

}
