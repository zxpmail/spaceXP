package cn.piesat.tools.generator.controller;

import cn.piesat.tools.generator.service.TableService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
