package cn.piesat.framework.mybatis.plus.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p/>
 * {@code @description}  :配置属性
 * <p/>
 * <b>@create:</b> 2023/1/12 17:14.
 *
 * @author zhouxp
 */
@ConfigurationProperties(prefix = "space.db")
@Data
public class MybatisPlusConfigProperties {

    private String createTime = "createTime"; //表数据创建时间字段

    private String updateTime = "updateTime"; //表数据更新时间字段

    private String createId = "createId"; //表数据创建者ID字段

    private String updateId = "updateId";  //表数据更新者ID字段

    private String deleted = "deleted";  //表数据逻辑删除字段 默认值0 删除为1

    private String deptId = "deptId"; //表数据创建者所属部门ID字段

    private String dbType = "mysql"; //操作数据库类型 如mysql、postgres等

    private String tenantId = "tenantId";//租户ID

    private String dateFormat = "yyyy-MM-dd HH:mm:ss";//LocalDateTime时间格式

    private Boolean dateFormatEnable = false;//是否启动时间格式转换

    private Boolean defaultFill = false;//是否启动默认填充值
}
