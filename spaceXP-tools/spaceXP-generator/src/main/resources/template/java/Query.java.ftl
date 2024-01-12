package ${package}.${moduleName}.model.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

<#list importList as i>
import ${i!};
</#list>

/**
* <p/>
* {@code @description}  : ${tableComment}查询
* <p/>
* <b>@create:</b> ${openingTime?string["yyyy-MM-dd hh:mm:ss a"]}
* <b>@email:</b> ${email}
*
* @author    ${author}
* @version   ${version}
*/

@Data
@ApiModel("${tableComment}查询对象")
public class ${className?cap_first}Query  {
<#list queryList as field>
    <#if field.fieldComment!?length gt 0>
    /**
    * ${field.fieldComment}
    */
    @ApiModelProperty(description = "${field.fieldComment}")
    </#if>
    <#if field.queryFormType == 'LocalDate'>
    @DateTimeFormat(pattern="yyyy-MM-dd")
    <#elseif field.queryFormType == 'LocalDateTime'>
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    </#if>
    <#if field.queryType == 'between'>
    private ${field.attrType}   start${field.attrName?cap_first};
    private ${field.attrType}   end${field.attrName?cap_first};
    <#else>
    private ${field.attrType}   ${field.attrName};
    </#if>
</#list>
}