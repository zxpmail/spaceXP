package cn.piesat.tools.generator.controller;

import cn.piesat.framework.common.annotation.validator.group.UpdateGroup;
import cn.piesat.framework.common.model.dto.PageBean;
import cn.piesat.framework.common.model.vo.PageResult;
import cn.piesat.tools.generator.model.query.TableQuery;
import cn.piesat.tools.generator.model.vo.TableFieldVO;
import cn.piesat.tools.generator.model.vo.TableVO;
import cn.piesat.tools.generator.service.TableFieldService;
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
@RequestMapping("table/field")
@AllArgsConstructor
public class TableFieldController {
    private final TableFieldService tableFieldService;


    /**
     * 更新字段类型
     * @param tableFields 表vo
     * @return  成功true 失败false
     */
    @PutMapping("update")
    public Boolean update(@RequestBody List<TableFieldVO> tableFields) {
        return tableFieldService.update(tableFields);
    }

}
