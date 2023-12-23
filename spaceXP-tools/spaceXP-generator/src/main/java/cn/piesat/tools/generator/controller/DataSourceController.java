package cn.piesat.tools.generator.controller;

import cn.piesat.framework.common.annotation.validator.group.AddGroup;
import cn.piesat.framework.common.annotation.validator.group.UpdateGroup;
import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;
import cn.piesat.tools.generator.model.entity.TableDO;
import cn.piesat.tools.generator.model.query.DataSourceQuery;
import cn.piesat.tools.generator.model.vo.DataSourceVO;
import cn.piesat.tools.generator.service.DataSourceService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p/>
 * {@code @description}  :数据源控制类
 * <p/>
 * <b>@create:</b> 2023/12/4 13:51.
 *
 * @author zhouxp
 */
@RestController
@RequestMapping("datasource")
@AllArgsConstructor
public class DataSourceController {
    private final DataSourceService dataSourceService;


    /**
     * 分页查询
     * @param pageBean 分页实体类
     * @param dataSourceQuery 数据源参数
     * @return 查询结果
     */
    @PostMapping("list")
    public PageResult page( PageBean pageBean, @RequestBody(required = false) DataSourceQuery dataSourceQuery) {
        return dataSourceService.list(pageBean,dataSourceQuery);
    }

    /**
     * 根据id查询数据源
     * @param id 数据源ID
     * @return 数据源实体
     */
    @GetMapping("/info/{id}")
    public DataSourceVO info(@PathVariable("id") Long id){
        return dataSourceService.info(id);
    }


    /**
     * 新增数据源
     * @param dataSourceVO 数据源DTO
     * @return 成功true 失败false
     */
    @PostMapping("add")
    public Boolean add(@Validated(AddGroup.class) @RequestBody DataSourceVO dataSourceVO) {
        return dataSourceService.add(dataSourceVO);
    }

    /**
     * 更新数据源
     * @param dataSourceVO 数据源DTO
     * @return  成功true 失败false
     */
    @PutMapping ("update")
    public Boolean update(@Validated(UpdateGroup.class) @RequestBody DataSourceVO dataSourceVO) {
        return dataSourceService.update(dataSourceVO);
    }

    /**
     * 根据集合中的Id删除记录
     * @param ids 数据源集合id
     * @return  成功true 失败false
     */

    @DeleteMapping("/delete")
    public Boolean delete(@RequestBody List<Long> ids) {
        return dataSourceService.delete(ids);
    }

    /**
     * 根据数据源id删除记录
     * @param id 数据源Id
     * @return  成功true 失败false
     */
    @DeleteMapping("/delete/{id}")
    public Boolean delete(@PathVariable Long id) {
        return dataSourceService.delete(id);
    }


    @PostMapping("test")
    public Boolean test(@Validated(UpdateGroup.class) @RequestBody DataSourceVO dataSourceVO) {
        return dataSourceService.test(dataSourceVO);
    }

    @GetMapping("table/list/{id}")
    public List<TableDO> tableList(@PathVariable("id") Long id) {
        return dataSourceService.tableList(id);
    }
}
