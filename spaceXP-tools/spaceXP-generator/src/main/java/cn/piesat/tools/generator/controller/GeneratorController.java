package cn.piesat.tools.generator.controller;

import cn.piesat.tools.generator.service.GeneratorService;
import cn.piesat.tools.generator.service.TableService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p/>
 * {@code @description}  :生成代码控制类
 * <p/>
 * <b>@create:</b> 2023/12/4 13:51.
 *
 * @author zhouxp
 */
@RestController
@RequestMapping("generator")
@AllArgsConstructor
public class GeneratorController {
    private final GeneratorService generatorService;
}
