package ${package}.${moduleName}.model.vo;

<#if castVoImportList?seq_contains("LocalDate") || castVoImportList?seq_contains("LocalDateTime") >
import com.fasterxml.jackson.annotation.JsonFormat;
</#if>
<#if castVoImportList?seq_contains("Long") >
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
</#if>
import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

<#list voImportList as i>
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
@ApiModel("${tableComment}VO")
public class ${className?cap_first}VO implements Serializable {
private static final long serialVersionUID = 1L;

<#list voList as field>
    <#if field.fieldComment!?length gt 0>
    /*
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
    private ${field.attrType} ${field.attrName};

</#list>

}