package cn.piesat.tools.generator.controller;

import cn.piesat.framework.common.annotation.validator.group.UpdateGroup;
import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;
import cn.piesat.tools.generator.model.dto.TableDTO;
import cn.piesat.tools.generator.model.query.TableQuery;
import cn.piesat.tools.generator.model.vo.TableVO;
import cn.piesat.tools.generator.service.TableService;
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
 * {@code @description}  :表结构控制类
 * <p/>
 * <b>@create:</b> 2023/12/4 13:51.
 *
 * @author zhouxp
 */
@RestController
@RequestMapping("table")
@AllArgsConstructor
public class TableController {
    private final TableService tableService;

    /**
     * 分页查询
     * @param pageBean 分页实体类
     * @param tableQuery 表查询参数
     * @return 查询结果
     */
    @PostMapping("list")
    public PageResult page(PageBean pageBean, @RequestBody TableQuery tableQuery) {
        return tableService.list(pageBean,tableQuery);
    }

    /**
     * 导入数据源中的表
     *
     * @param tableList 表名列表
     */
    @PostMapping("add")
    public Boolean add(@RequestBody List<TableDTO> tableList) {
        return tableService.add(tableList);
    }

    @DeleteMapping("/delete")
    public Boolean delete(@RequestBody List<Long> ids) {
        return tableService.delete(ids);
    }

    /**
     * 根据字段类型id删除记录
     * @param id 数据源Id
     * @return  成功true 失败false
     */
    @DeleteMapping("/delete/{id}")
    public Boolean delete(@PathVariable Long id) {
        return tableService.delete(id);
    }


    /**
     * 同步表结构
     *
     * @param tableDTO 表信息
     */
    @PostMapping("sync")
    public Boolean sync(@RequestBody TableDTO tableDTO) {
        return tableService.sync(tableDTO);
    }

    /**
     * 根据id查询字段类型
     * @param id 字段类型ID
     * @return 字段类型实体
     */
    @GetMapping("/info/{id}")
    public TableVO info(@PathVariable("id") Long id){
        return tableService.info(id);
    }



    /**
     * 更新字段类型
     * @param tableDTO 表dto
     * @return  成功true 失败false
     */
    @PutMapping("update")
    public Boolean update(@Validated(UpdateGroup.class) @RequestBody TableDTO tableDTO ) {
        return tableService.update(tableDTO);
    }

}
