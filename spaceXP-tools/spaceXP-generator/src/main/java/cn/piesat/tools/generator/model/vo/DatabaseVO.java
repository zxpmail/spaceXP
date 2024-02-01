package cn.piesat.tools.generator.model.vo;

import cn.piesat.framework.common.annotation.validator.group.AddGroup;
import cn.piesat.framework.common.annotation.validator.group.UpdateGroup;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * <p/>
 * {@code @description}  :数据库信息实体类
 * <p/>
 * <b>@create:</b> 2023/12/5 10:37.
 *
 * @author zhouxp
 */
@Data
public class DatabaseVO {
    /**
     * ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull(message = "主键不能为空", groups = UpdateGroup.class)
    private Long id;

    /**
     * 数据库类型
     */
    /**
     * 是否拼接数据库名称 0否 1是 由于ck数据库直接连接要增加表名 默认ck为 其他数据库为0
     */
    private Integer addDatabaseName;

    @NotBlank(message = "数据库类型不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 50 , message = "长度必须小于等于50" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String dbType;
    /**
     * 驱动类
     */
    @NotBlank(message = "驱动类型不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 50 , message = "长度必须小于等于50" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String driverClassName;

    /**
     * 表字段信息查询 SQL
     */
    @NotBlank(message = "表字段信息查询 SQL不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String tableFields;
    /**
     * 表信息查询SQL
     */
    @NotBlank(message = "表信息查询SQL不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String tableSql;

    /**
     * url
     */
    @NotBlank(message = "url不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 256 , message = "长度必须小于等于128" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String url;

}
