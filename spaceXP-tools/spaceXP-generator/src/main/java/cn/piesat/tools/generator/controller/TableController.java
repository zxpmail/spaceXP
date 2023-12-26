package cn.piesat.tools.generator.controller;

import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;
import cn.piesat.tools.generator.model.query.TableQuery;
import cn.piesat.tools.generator.model.vo.TableVO;
import cn.piesat.tools.generator.service.TableService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
     * @param datasourceId  数据源ID
     * @param tableList 表名列表
     */
    @PostMapping("import/{datasourceId}")
    public void tableImport(@PathVariable("datasourceId") Long datasourceId, @RequestBody List<TableVO> tableList) {
        tableService.tableImport(datasourceId, tableList);
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

}
