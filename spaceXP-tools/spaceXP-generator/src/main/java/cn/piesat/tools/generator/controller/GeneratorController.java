package cn.piesat.tools.generator.controller;

import cn.hutool.core.io.IoUtil;
import cn.piesat.tools.generator.model.dto.TableDTO;
import cn.piesat.tools.generator.service.GeneratorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipOutputStream;

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
}
