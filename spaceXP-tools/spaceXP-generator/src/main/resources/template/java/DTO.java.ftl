package ${package}.${moduleName}.model.dto;

import cn.piesat.framework.common.annotation.validator.group.AddGroup;
import cn.piesat.framework.common.annotation.validator.group.UpdateGroup;

<#if castDtoImportList?seq_contains("LocalDate") || castDtoImportList?seq_contains("LocalDateTime") >
import com.fasterxml.jackson.annotation.JsonFormat;
</#if>
<#if castDtoImportList?seq_contains("Long") >
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
</#if>
import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import org.hibernate.validator.constraints.*;
import javax.validation.constraints.*;

<#list dtoImportList as i>
import ${i!};
</#list>

/**
* <p/>
* {@code @description}  : ${tableComment}
* <p/>
* <b>@create:</b> ${openingTime}
* <b>@email:</b> ${email}
*
* @author    ${author}
* @version   ${version}
*/

@Data
@ApiModel("${tableComment}DTO")
public class ${className?cap_first}DTO implements Serializable {
private static final long serialVersionUID = 1L;

<#list dtoList as field>
    <#if field.fieldComment!?length gt 0>
    /**
    * ${field.fieldComment}
    */
    @ApiModelProperty("${field.fieldComment}")
    </#if>
    <#if field.attrType == 'LocalDate'>
    @JsonFormat(pattern="yyyy-MM-dd")
    <#elseif field.attrType == 'LocalDateTime'>
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    <#elseif field.attrType == 'Long'>
    @JsonSerialize(using= ToStringSerializer.class)
    </#if>
    <#if field.primaryPk == 1>
        <#if field.attrType == 'String'>
    @NotBlank(message = "主键不能为空", groups = UpdateGroup.class)
        <#else>
    @NotNull(message = "主键不能为空", groups = UpdateGroup.class)
        </#if>
    </#if>
    <#if field.len != 0>
    @Length(max = ${field.len} , message = "长度必须小于等于${field.len}" ,groups ={AddGroup.class,UpdateGroup.class} )
    </#if>
    <#if field.formRequired == 1>
        <#if field.attrType == 'String'>
    @NotNull(message = "${field.fieldComment}不能为空" , groups = {AddGroup.class, UpdateGroup.class})
        <#else>
    @NotBlank(message = "${field.fieldComment}不能为空", groups = {AddGroup.class, UpdateGroup.class})
        </#if>
    </#if>
    private ${field.attrType} ${field.attrName};
</#list>

}