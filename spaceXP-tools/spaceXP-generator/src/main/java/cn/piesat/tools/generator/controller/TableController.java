package cn.piesat.tools.generator.controller;

import cn.piesat.tools.generator.model.vo.TableVO;
import cn.piesat.tools.generator.service.TableService;
import lombok.AllArgsConstructor;
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
     * 导入数据源中的表
     *
     * @param datasourceId  数据源ID
     * @param tableNameList 表名列表
     */
    @PostMapping("import/{datasourceId}")
    public String tableImport(@PathVariable("datasourceId") Long datasourceId, @RequestBody List<TableVO> tableNameList) {
            return tableService.tableImport(datasourceId, tableNameList);
    }

}