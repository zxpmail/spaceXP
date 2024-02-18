package cn.piesat.tools.generator.controller;

import cn.piesat.tools.generator.model.dto.ImportDataSourceDTO;
import cn.piesat.tools.generator.model.vo.TableVO;
import cn.piesat.tools.generator.service.ImportDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p/>
 * {@code @description}  :导入数据控制类
 * <p/>
 * <b>@create:</b> 2024/1/18 13:45.
 *
 * @author zhouxp
 */
@RestController
@RequestMapping("import")
@RequiredArgsConstructor
public class ImportDataController {
    private final ImportDataService importDataService;

    /**
     * 得到该数据源所有表
     *
     * @param importDataSourceDTO  数据源DTO
     */
    @PostMapping("all")
    public List<TableVO> getAllTablesByDataSource(@Validated @RequestBody ImportDataSourceDTO importDataSourceDTO) {
        return importDataService.getAllTablesByDataSource(importDataSourceDTO);
    }

}
