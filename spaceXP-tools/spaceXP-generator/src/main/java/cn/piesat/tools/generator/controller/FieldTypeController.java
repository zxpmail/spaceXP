package cn.piesat.tools.generator.controller;

import cn.piesat.tools.generator.service.FieldTypeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p/>
 * {@code @description}  :字段类型控制类
 * <p/>
 * <b>@create:</b> 2023/12/4 13:51.
 *
 * @author zhouxp
 */
@RestController
@RequestMapping("fieldType")
@AllArgsConstructor
public class FieldTypeController {
    private final FieldTypeService fieldTypeService;
}
