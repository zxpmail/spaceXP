package ${package}.${moduleName}.controller;

import java.util.List;

import cn.piesat.framework.common.annotation.validator.group.AddGroup;
import cn.piesat.framework.common.annotation.validator.group.UpdateGroup;
import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;
import ${package}.${moduleName}.model.dto.${className?cap_first}DTO;
import ${package}.${moduleName}.model.query.${className?cap_first}Query;
import ${package}.${moduleName}.service.${className?cap_first}Service;
import ${package}.${moduleName}.model.entity.${className?cap_first}VO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

@Api(tags = "${tableComment}")
@RestController
@RequestMapping("${functionName}")
@RequiredArgsConstructor
public class ${className?cap_first}Controller {

    private final ${className?cap_first}Service ${className}Service;

    @ApiOperation("分页查询")
    @PostMapping("/list")
    public PageResult list(PageBean pageBean, @RequestBody(required = false) ${className?cap_first}Query ${className}Query){
        return ${className}Service.list(pageBean,${className}Query);
    }

    @ApiOperation("根据id查询")
    @GetMapping("/info/{id}")
    public ${className?cap_first}VO info(@PathVariable("id") ${pkType} id){
        return ${className}Service.info(id);
    }

    @ApiOperation("保存信息")
    @PostMapping("/save")
    public Boolean save(@Validated(AddGroup.class) @RequestBody ${className?cap_first}DTO ${className}DTO){
        return ${className}Service.save(${className}DTO);
    }

    @ApiOperation("修改信息")
    @PutMapping("/update")
    public Boolean update(@Validated(UpdateGroup.class) @RequestBody ${className?cap_first}DTO ${className}DTO){
        return ${className}Service.update(${className}DTO);
    }

    @ApiOperation("批量删除信息")
    @DeleteMapping("/delete")
    public Boolean delete(@RequestBody List<${pkType}> ids){
        return ${className}Service.delete(ids);
    }

    @ApiOperation("根据id删除信息")
    @DeleteMapping("/delete/{id}")
    public Boolean delete(@PathVariable ${pkType} id){
        return ${className}Service.delete(id);
    }
}