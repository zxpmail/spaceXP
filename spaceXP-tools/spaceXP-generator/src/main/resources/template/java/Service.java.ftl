package ${package}.${moduleName}.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;
import ${package}.${moduleName}.model.entity.${className?cap_first}DO;
import ${package}.${moduleName}.model.dto.${className?cap_first}DTO;
import ${package}.${moduleName}.model.query.${className?cap_first}Query;
import ${package}.${moduleName}.model.vo.${className?cap_first}VO;

import java.util.List;

/**
* <p/>
* {@code @description}  : ${tableComment}Service接口
* <p/>
* <b>@create:</b> ${openingTime}
* <b>@email:</b> ${email}
*
* @author    ${author}
* @version   ${version}
*/

public interface ${className?cap_first}Service extends IService<${className?cap_first}DO> {

    /**
    * 分页查询
    *
    * @param pageBean {@link PageBean} 分页对象
    * @param ${className}Query {@link ${className?cap_first}Query} 用户信息表查询对象
    * @return {@link PageResult} 查询结果
    */
    PageResult list(PageBean pageBean, ${className?cap_first}Query ${className}Query);

    /**
    * 根据id查询
    *
    * @param id id
    * @return {@link ${className?cap_first}VO}
    */
    ${className?cap_first}VO info(${pkType} id);

    /**
    * 新增
    *
    * @param ${className}DTO {@link ${className?cap_first}DTO} 用户信息表DTO
    * @return false or true
    */
    Boolean save(${className?cap_first}DTO ${className}DTO);

    /**
    * 修改
    *
    * @param ${className}DTO {@link ${className?cap_first}DTO} 用户信息表DTO
    * @return false or true
    */
    Boolean update(${className?cap_first}DTO ${className}DTO);

    /**
    * 批量删除
    *
    * @param ids id集合
    * @return false or true
    */
    Boolean delete(List<${pkType}> ids);

    /**
    * 根据id删除
    *
    * @param id id
    * @return false or true
    */
    Boolean delete(${pkType} id);
    }