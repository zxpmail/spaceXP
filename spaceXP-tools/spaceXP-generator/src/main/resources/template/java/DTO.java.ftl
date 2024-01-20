package ${package}.${moduleName}.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
	</#if>
	private ${field.attrType} ${field.attrName};

</#list>

}