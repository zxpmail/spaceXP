package cn.piesat.tools.generator.controller;

import cn.piesat.tools.generator.model.dto.TableDTO;
import cn.piesat.tools.generator.model.dto.TablesDTO;
import cn.piesat.tools.generator.service.GeneratorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

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

    /**
     * 生成代码（zip压缩包）
     */
    @PostMapping("/code")
    public void code(@RequestBody TableDTO tableDTO, HttpServletResponse response){
        generatorService.generatorCode(tableDTO,response);
    }

    /**
     * 批量生成代码（zip压缩包）
     */
    @PostMapping("/code/batch")
    public void batchGenerator(@RequestBody TablesDTO tablesDTO, HttpServletResponse response){
         generatorService.generatorCode(tablesDTO,response);
    }
}
