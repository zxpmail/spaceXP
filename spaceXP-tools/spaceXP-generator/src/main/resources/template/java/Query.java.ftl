package ${package}.${moduleName}.model.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;
<#if castQueryImportList?seq_contains("LocalDate") || castQueryImportList?seq_contains("LocalDateTime") >
import com.fasterxml.jackson.annotation.JsonFormat;
</#if>
<#list queryImportList as i>
import ${i!};
</#list>

/**
* <p/>
* {@code @description}  : ${tableComment}查询
* <p/>
* <b>@create:</b> ${openingTime}
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
    @ApiModelProperty("${field.fieldComment}")
    </#if>
    <#if field.attrType == 'LocalDate'>
    @JsonFormat(pattern="yyyy-MM-dd")
    <#elseif field.attrType == 'LocalDateTime'>
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    </#if>
    <#if field.queryType == 'between'>
    private ${field.attrType}   start${field.attrName?cap_first};
    /**
    * 结束${field.fieldComment}
    */
    @ApiModelProperty("结束${field.fieldComment}")
        <#if field.attrType == 'LocalDate'>
    @JsonFormat(pattern="yyyy-MM-dd")
        <#elseif field.attrType == 'LocalDateTime'>
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
        </#if>
    private ${field.attrType}   end${field.attrName?cap_first};
    <#else>
    private ${field.attrType}   ${field.attrName};
    </#if>
</#list>
<#list orderList as field>
    /*
    * 根据${field.fieldComment}排序  false降序 true升序
    */
    private Boolean order${field.attrName?cap_first};
</#list>
}