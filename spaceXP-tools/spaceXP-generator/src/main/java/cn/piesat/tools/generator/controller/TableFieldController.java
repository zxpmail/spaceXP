package cn.piesat.tools.generator.controller;

import cn.piesat.tools.generator.model.dto.TableFieldDTO;
import cn.piesat.tools.generator.service.TableFieldService;
import lombok.AllArgsConstructor;
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
    public Boolean update(@RequestBody List<TableFieldDTO> tableFields) {
        return tableFieldService.update(tableFields);
    }

}
