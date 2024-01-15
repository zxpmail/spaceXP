package ${package}.${moduleName}.dao.mapper;

import ${package}.${moduleName}.model.entity.${className?cap_first}DO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* <p/>
* {@code @description}  : ${tableComment}
* <p/>
* <b>@create:</b> ${openingTime?string["yyyy-MM-dd hh:mm:ss a"]}
* <b>@email:</b> ${email}
*
* @author    ${author}
* @version   ${version}
*/

@Mapper
public interface ${className?cap_first}Mapper extends BaseMapper<${className?cap_first}DO> {

}