package cn.piesat.tools.generator.model.query;

import cn.piesat.framework.common.annotation.validator.group.AddGroup;
import cn.piesat.framework.common.annotation.validator.group.UpdateGroup;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p/>
 * {@code @description}  :
 * <p/>
 * <b>@create:</b> 2023/12/6 9:03.
 *
 * @author zhouxp
 */
@Data
public class FieldTypeVO {
    /**
     * id
     */
    @NotNull(message = "主键id不能为空", groups = UpdateGroup.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 字段类型
     */
    @NotBlank(message = "字段类型不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 50 , message = "长度必须小于等于50" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String columnType;
    /**
     * 属性类型
     */
    @NotBlank(message = "属性类型不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 50 , message = "长度必须小于等于50" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String attrType;
    /**
     * 属性包名
     */
    @NotBlank(message = "属性包名不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 50 , message = "长度必须小于等于50" ,groups ={AddGroup.class,UpdateGroup.class} )
    private String packageName;

    /**
     * 是否列表显示，针对blob等等大字段不建议列表显示 1显示 0不显现
     */
    @NotNull(message = "是否列表显示不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Integer isList;
}
