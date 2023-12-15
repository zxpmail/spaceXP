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
     * 表名称
     */
    @NotBlank(message = "表名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 50 , message = "长度必须小于等于50" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String tableName;
    /**
     * 表注释
     */
    @NotBlank(message = "表注释不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 50 , message = "长度必须小于等于50" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String tableComment;
    /**
     * 字段名称
     */
    @NotBlank(message = "字段名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 50 , message = "长度必须小于等于50" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String fieldName;
    /**
     *  字段类型
     */
    @NotBlank(message = "字段类型不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 50 , message = "长度必须小于等于50" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String fieldType;
    /**
     * 字段注释
     */
    @NotBlank(message = "字段注释不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 50 , message = "长度必须小于等于50" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String fieldComment;
    /**
     * 主键字段
     */
    @NotBlank(message = "主键字段不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 50 , message = "长度必须小于等于50" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String fieldKey;
    /**
     * 表字段信息查询 SQL
     */
    @NotBlank(message = "表字段信息查询 SQL不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 4096 , message = "长度必须小于等于4096" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String tableFields;
    /**
     * 表信息查询SQL
     */
    @NotBlank(message = "表信息查询SQL不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 512 , message = "长度必须小于等于512" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String tableSql;
    /**
     * 表信息查询表名SQL
     */
    @NotBlank(message = "表信息查询SQL不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 128 , message = "长度必须小于等于128" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String tableNameSql;
    /**
     * 表信息查询附加SQL
     */
    @Length(max = 256 , message = "长度必须小于等于256" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String tableAddSql;
    /**
     * url
     */
    @NotBlank(message = "url不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 128 , message = "长度必须小于等于128" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String url;

}
