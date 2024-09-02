package ${package}.${moduleName}.service.impl;

import cn.piesat.framework.common.exception.BaseException;
import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.enums.CommonResponseEnum;
import cn.piesat.framework.common.model.vo.PageResult;

import cn.piesat.framework.common.utils.CopyBeanUtils;
import cn.piesat.framework.mybatis.plus.utils.QueryUtils;
import ${package}.${moduleName}.model.dto.${className?cap_first}DTO;
import ${package}.${moduleName}.model.query.${className?cap_first}Query;
import ${package}.${moduleName}.model.vo.${className?cap_first}VO;
import ${package}.${moduleName}.mapper.${className?cap_first}Mapper;
import ${package}.${moduleName}.model.entity.${className?cap_first}DO;
import ${package}.${moduleName}.service.${className?cap_first}Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Objects;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.util.StringUtils;
import org.springframework.util.CollectionUtils;

/**
* <p/>
* {@code @description}  : ${tableComment}Service实现类
* <p/>
* <b>@create:</b> ${openingTime}
* <b>@email:</b> ${email}
*
* @author    ${author}
* @version   ${version}
*/

@Service("${className}Service")
public class ${className?cap_first}ServiceImpl extends ServiceImpl<${className?cap_first}Mapper, ${className?cap_first}DO> implements ${className?cap_first}Service {

    @Override
    public PageResult list(PageBean pageBean, ${className?cap_first}Query ${className}Query) {
        IPage<${className?cap_first}DO> page ;
        if (Objects.isNull(${className}Query)) {
            page = this.page(QueryUtils.getPage(pageBean));
        } else {
            page = this.page(QueryUtils.getPage(pageBean), getWrapper(${className}Query));
        }
        return new PageResult(page.getTotal(), CopyBeanUtils.copy(page.getRecords(), ${className?cap_first}VO::new));
    }

    private LambdaQueryWrapper<${className?cap_first}DO> getWrapper(${className?cap_first}Query ${className}Query){
        LambdaQueryWrapper<${className?cap_first}DO> wrapper = Wrappers.lambdaQuery();
        <#list queryList as field>
            <#if field.queryType == 'between'>
                <#if field.attrType=='String'>
        if(StringUtils.hasText(${className}Query.getStart${field.attrName?cap_first}()) &&StringUtils.hasText(${className}Query.getEnd${field.attrName?cap_first}())){
            wrapper.between(${className?cap_first}DO::get${field.attrName?cap_first}, ${className}Query.getStart${field.attrName?cap_first}(),${className}Query.getEnd${field.attrName?cap_first}());
        }
                <#else>
        if(!Objects.isNull(${className}Query.getStart${field.attrName?cap_first}())&&!Objects.isNull(${className}Query.getEnd${field.attrName?cap_first}())){
            wrapper.between(${className?cap_first}DO::get${field.attrName?cap_first}, ${className}Query.getStart${field.attrName?cap_first}(),${className}Query.getEnd${field.attrName?cap_first}());
        }
                </#if>
            <#elseif field.queryType == '='>
                <#if field.attrType=='String'>
        wrapper.eq(StringUtils.hasText(${className}Query.get${field.attrName?cap_first}()), ${className?cap_first}DO::get${field.attrName?cap_first}, ${className}Query.get${field.attrName?cap_first}());
                <#else>
        wrapper.eq(!Objects.isNull(${className}Query.get${field.attrName?cap_first}()), ${className?cap_first}DO::get${field.attrName?cap_first}, ${className}Query.get${field.attrName?cap_first}());
                </#if>
            <#elseif field.queryType == '!='>
                <#if field.attrType=='String'>
        wrapper.ne(StringUtils.hasText(${className}Query.get${field.attrName?cap_first}()), ${className?cap_first}DO::get${field.attrName?cap_first}, ${className}Query.get${field.attrName?cap_first}());
                <#else>
        wrapper.ne(!Objects.isNull(${className}Query.get${field.attrName?cap_first}()), ${className?cap_first}DO::get${field.attrName?cap_first}, ${className}Query.get${field.attrName?cap_first}());
                </#if>
            <#elseif field.queryType == '>'>
                <#if field.attrType=='String'>
        wrapper.gt(StringUtils.hasText(${className}Query.get${field.attrName?cap_first}()), ${className?cap_first}DO::get${field.attrName?cap_first}, ${className}Query.get${field.attrName?cap_first}());
                <#else>
        wrapper.gt(!Objects.isNull(${className}Query.get${field.attrName?cap_first}()), ${className?cap_first}DO::get${field.attrName?cap_first}, ${className}Query.get${field.attrName?cap_first}());
                </#if>
            <#elseif field.queryType == '>='>
                <#if field.attrType=='String'>
        wrapper.ge(StringUtils.hasText(${className}Query.get${field.attrName?cap_first}()), ${className?cap_first}DO::get${field.attrName?cap_first}, ${className}Query.get${field.attrName?cap_first}());
                <#else>
        wrapper.ge(!Objects.isNull(${className}Query.get${field.attrName?cap_first}()), ${className?cap_first}DO::get${field.attrName?cap_first}, ${className}Query.get${field.attrName?cap_first}());
                </#if>
            <#elseif field.queryType == '<'>
                <#if field.attrType=='String'>
        wrapper.lt(StringUtils.hasText(${className}Query.get${field.attrName?cap_first}()), ${className?cap_first}DO::get${field.attrName?cap_first}, ${className}Query.get${field.attrName?cap_first}());
                <#else>
        wrapper.lt(!Objects.isNull(${className}Query.get${field.attrName?cap_first}()), ${className?cap_first}DO::get${field.attrName?cap_first}, ${className}Query.get${field.attrName?cap_first}());
                </#if>
            <#elseif field.queryType == '<='>
                <#if field.attrType=='String'>
        wrapper.le(StringUtils.hasText(${className}Query.get${field.attrName?cap_first}()), ${className?cap_first}DO::get${field.attrName?cap_first}, ${className}Query.get${field.attrName?cap_first}());
                <#else>
        wrapper.le(!Objects.isNull(${className}Query.get${field.attrName?cap_first}()), ${className?cap_first}DO::get${field.attrName?cap_first}, ${className}Query.get${field.attrName?cap_first}());
                </#if>
            <#elseif field.queryType == 'like'>
                <#if field.attrType=='String'>
        wrapper.like(StringUtils.hasText(${className}Query.get${field.attrName?cap_first}()), ${className?cap_first}DO::get${field.attrName?cap_first}, ${className}Query.get${field.attrName?cap_first}());
                </#if>
            <#elseif field.queryType == 'left like'>
                <#if field.attrType=='String'>
        wrapper.likeLeft(StringUtils.hasText(${className}Query.get${field.attrName?cap_first}()), ${className?cap_first}DO::get${field.attrName?cap_first}, ${className}Query.get${field.attrName?cap_first}());
                </#if>
            <#elseif field.queryType == 'right like'>
                <#if field.attrType=='String'>
        wrapper.likeRight(StringUtils.hasText(${className}Query.get${field.attrName?cap_first}()), ${className?cap_first}DO::get${field.attrName?cap_first}, ${className}Query.get${field.attrName?cap_first}());
                </#if>
            </#if>
        </#list>

        <#list orderList as field>
        if(!Objects.isNull(${className}Query.getOrder${field.attrName?cap_first}())) {
            wrapper.orderBy(true, ${className}Query.getOrder${field.attrName?cap_first}(), ${className?cap_first}DO::get${field.attrName?cap_first});
        }
        </#list>
        <#if select?has_content>
        wrapper.select(${className?cap_first}DO.class,${select});
        </#if>
        return wrapper;
    }

    @Override
    public ${className?cap_first}VO info(${pkType} id) {
        return CopyBeanUtils.copy(getById(id),${className?cap_first}VO::new);
    }
<#if repeatList?? && (repeatList?size > 0) >
    void repeat(${className?cap_first}DTO ${className}DTO,Boolean isAdd) {
        LambdaQueryWrapper<${className?cap_first}DO> wrapper = new LambdaQueryWrapper<>();
        <#list repeatList as field>
        <#if field.attrType=='String'>
        wrapper.eq(StringUtils.hasText(${className}DTO.get${field.attrName?cap_first}()), ${className?cap_first}DO::get${field.attrName?cap_first}, ${className}DTO.get${field.attrName?cap_first}());
        <#else>
        wrapper.eq(!Objects.isNull(${className}DTO.get${field.attrName?cap_first}()), ${className?cap_first}DO::get${field.attrName?cap_first}, ${className}DTO.get${field.attrName?cap_first}());
        </#if>
        </#list>
        if(!isAdd) {
            wrapper.ne(${className?cap_first}DO::get${pk?cap_first},${className}DTO.get${pk?cap_first}());
        }else if(CollectionUtils.isEmpty(wrapper.getExpression().getNormal())){
            return;
        }
        if (count(wrapper) > 0) {
            throw  new BaseException(CommonResponseEnum.RECORD_REPEAT);
        }
    }
</#if>
    @Override
    public Boolean save(${className?cap_first}DTO ${className}DTO) {
        <#if repeatList?? && (repeatList?size > 0) >
        repeat(${className}DTO,true);
        </#if>
        ${className?cap_first}DO ${className}DO = CopyBeanUtils.copy(${className}DTO, ${className?cap_first}DO::new);
        return save(${className}DO);
    }

    @Override
    public Boolean update(${className?cap_first}DTO ${className}DTO) {
        <#if repeatList?? && (repeatList?size > 0) >
        repeat(${className}DTO,false);
        </#if>
        ${className?cap_first}DO ${className}DO = this.getById(${className}DTO.get${pk?cap_first}());
        BeanUtils.copyProperties(${className}DTO, ${className}DO,CopyBeanUtils.getNullPropertyNames(${className}DTO));
        return updateById(${className}DO);
    }

    @Override
    public Boolean delete(List<${pkType}> ids) {
        return removeBatchByIds(ids);
    }

    @Override
    public Boolean delete(${pkType} id) {
        return removeById(id);
    }
}