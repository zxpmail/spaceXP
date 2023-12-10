package cn.piesat.dynamic.datasource.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2023/12/9 19:55.
 *
 * @author zhouxp
 */
@Data
@TableName("db_info")
public class DbInfo {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 数据库地址
     */
    private String url;
    /**
     * 数据库用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 数据库驱动
     */
    private String driverClassName;
    /**
     * 数据库name，即保存Map中的key
     */
    private String name;
}
