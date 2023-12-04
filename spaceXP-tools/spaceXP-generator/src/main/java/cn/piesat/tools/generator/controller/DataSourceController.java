package cn.piesat.tools.generator.controller;

import cn.piesat.tools.generator.service.DataSourceService;
import cn.piesat.tools.generator.service.FieldTypeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
