package ${package}.${moduleName}.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
<#list importList as i>
import ${i!};
</#list>

/**
 * ${tableComment}
 *
 * @author ${author} ${email}
 * @since ${version} ${date}
 */
@Data
@TableName("${tableName}")
public class ${ClassName}Entity {
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
    <#if field.primaryPk>
	@TableId
	</#if>
	private ${field.attrType} ${field.attrName};

</#list>
}