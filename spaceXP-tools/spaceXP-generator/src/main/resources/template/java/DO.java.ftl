package ${package}.${moduleName}.model.entity;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
<#list importList as i>
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
@TableName("${tableName}")
public class ${className?cap_first}DO {
<#list fieldList as field>
    <#if field.fieldComment!?length gt 0>
    /**
    * ${field.fieldComment}
    */
    </#if>
    <#if field.autoFill == "INSERT">
    @TableField(fill = FieldFill.INSERT)
    </#if>
    <#if field.autoFill == "INSERT_UPDATE">
    @TableField(fill = FieldFill.INSERT_UPDATE)
    </#if>
    <#if field.autoFill == "UPDATE">
    @TableField(fill = FieldFill.UPDATE)
    </#if>
    <#if field.primaryPk == 1>
    @TableId
    </#if>
    private ${field.attrType} ${field.attrName};

</#list>
}