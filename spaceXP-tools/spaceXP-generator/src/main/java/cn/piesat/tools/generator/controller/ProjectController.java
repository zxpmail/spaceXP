package cn.piesat.tools.generator.controller;

import cn.piesat.framework.common.annotation.validator.group.AddGroup;
import cn.piesat.framework.common.annotation.validator.group.UpdateGroup;
import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;
import cn.piesat.tools.generator.model.dto.ProjectDTO;
import cn.piesat.tools.generator.model.query.ProjectQuery;
import cn.piesat.tools.generator.model.vo.ProjectVO;
import cn.piesat.tools.generator.service.ProjectService;
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
 * {@code @description}  :项目结构控制类
 * <p/>
 * <b>@create:</b> 2023/12/4 13:51.
 *
 * @author zhouxp
 */
@RestController
@RequestMapping("project")
@AllArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    /**
     * 查询所有项目
     * @return 查询结果
     */
    @GetMapping("all")
    public List<ProjectVO> all() {
        return projectService.all();
    }
    /**
     * 分页查询
     * @param pageBean 分页实体类
     * @param projectQuery 字段类型参数
     * @return 查询结果
     */
    @PostMapping("list")
    public PageResult page(PageBean pageBean, @RequestBody ProjectQuery projectQuery) {
        return projectService.list(pageBean,projectQuery);
    }

    /**
     * 根据id查询字段类型
     * @param id 字段类型ID
     * @return 字段类型实体
     */
    @GetMapping("/info/{id}")
    public ProjectVO info(@PathVariable("id") Long id){
        return projectService.info(id);
    }


    /**
     * 新增字段类型
     * @param projectDTO 数据源DTO
     * @return 成功true 失败false
     */
    @PostMapping("add")
    public Boolean add(@Validated(AddGroup.class) @RequestBody ProjectDTO projectDTO) {
        return projectService.add(projectDTO);
    }

    /**
     * 更新字段类型
     * @param projectDTO 字段类型DTO
     * @return  成功true 失败false
     */
    @PutMapping("update")
    public Boolean update(@Validated(UpdateGroup.class) @RequestBody  ProjectDTO projectDTO) {
        return projectService.update(projectDTO);
    }

    /**
     * 根据集合中的Id删除记录
     * @param ids 字段类型id
     * @return  成功true 失败false
     */

    @DeleteMapping("/delete")
    public Boolean delete(@RequestBody List<Long> ids) {
        return projectService.delete(ids);
    }

    /**
     * 根据字段类型id删除记录
     * @param id 数据源Id
     * @return  成功true 失败false
     */
    @DeleteMapping("/delete/{id}")
    public Boolean delete(@PathVariable Long id) {
        return projectService.delete(id);
    }

}
