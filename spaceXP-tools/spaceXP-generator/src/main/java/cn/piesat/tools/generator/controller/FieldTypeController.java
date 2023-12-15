package cn.piesat.tools.generator.controller;

import cn.piesat.framework.common.annotation.validator.group.AddGroup;
import cn.piesat.framework.common.annotation.validator.group.UpdateGroup;
import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;
import cn.piesat.tools.generator.model.query.FieldTypeQuery;
import cn.piesat.tools.generator.model.vo.FieldTypeVO;
import cn.piesat.tools.generator.service.FieldTypeService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p/>
 * {@code @description}  :字段类型控制类
 * <p/>
 * <b>@create:</b> 2023/12/4 13:51.
 *
 * @author zhouxp
 */
@RestController
@RequestMapping("fieldType")
@AllArgsConstructor
public class FieldTypeController {
    private final FieldTypeService fieldTypeService;
    /**
     * 分页查询
     * @param pageBean 分页实体类
     * @param fieldTypeQuery 字段类型参数
     * @return 查询结果
     */
    @PostMapping("list")
    public PageResult page(PageBean pageBean, @RequestBody FieldTypeQuery fieldTypeQuery) {
        return fieldTypeService.list(pageBean,fieldTypeQuery);
    }

    /**
     * 根据id查询字段类型
     * @param id 字段类型ID
     * @return 字段类型实体
     */
    @GetMapping("/info/{id}")
    public FieldTypeVO info(@PathVariable("id") Long id){
        return fieldTypeService.info(id);
    }


    /**
     * 新增字段类型
     * @param fieldTypeVO 数据源DTO
     * @return 成功true 失败false
     */
    @PostMapping("add")
    public Boolean add(@Validated(AddGroup.class) @RequestBody FieldTypeVO fieldTypeVO) {
        return fieldTypeService.add(fieldTypeVO);
    }

    /**
     * 更新字段类型
     * @param fieldTypeVO 字段类型DTO
     * @return  成功true 失败false
     */
    @PutMapping("update")
    public Boolean update(@Validated(UpdateGroup.class) @RequestBody FieldTypeVO fieldTypeVO) {
        return fieldTypeService.update(fieldTypeVO);
    }

    /**
     * 根据集合中的Id删除记录
     * @param ids 字段类型id
     * @return  成功true 失败false
     */

    @DeleteMapping("/delete")
    public Boolean delete(@RequestBody List<Long> ids) {
        return fieldTypeService.delete(ids);
    }

    /**
     * 根据字段类型id删除记录
     * @param id 数据源Id
     * @return  成功true 失败false
     */
    @DeleteMapping("/delete/{id}")
    public Boolean delete(@PathVariable Long id) {
        return fieldTypeService.delete(id);
    }

}
