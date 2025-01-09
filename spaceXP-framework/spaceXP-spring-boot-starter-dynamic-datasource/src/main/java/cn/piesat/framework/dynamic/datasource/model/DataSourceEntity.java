package cn.piesat.framework.dynamic.datasource.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.security.PrivateKey;

/**
 * <p/>
 * {@code @description}  : 数据源实体
 * <p/>
 * <b>@create:</b> 2023/12/9 11:09.
 *
 * @author zhouxp
 */
@Data
@Accessors(chain = true)
public class DataSourceEntity {

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
     * 数据库key，即保存Map中的key
     */
    private String key;

    /**
     * 私钥
     */
    private String privateKey;
}
