package cn.piesat.tools.generator.controller;

import cn.hutool.core.io.IoUtil;
import cn.piesat.tools.generator.model.dto.TableDTO;
import cn.piesat.tools.generator.service.GeneratorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
    @GetMapping("download")
    public void download(String tableIds, HttpServletResponse response) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        // 生成代码
        for (String tableId : tableIds.split(",")) {
            //generatorService.downloadCode(Long.parseLong(tableId), zip);
        }

        IoUtil.close(zip);

        // zip压缩包数据
        byte[] data = outputStream.toByteArray();

        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"maku.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IoUtil.write(response.getOutputStream(), false, data);
    }

    @RequestMapping("/code")
    public void code(@RequestBody TableDTO tableDTO, HttpServletResponse response) throws IOException {
        try {
            byte[] responseData = generatorService.generatorCode(tableDTO);

            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=\"piesat.zip\"");
            response.addHeader("Content-Length", "" + responseData.length);
            response.setContentType("application/octet-stream; charset=UTF-8");

            IoUtil.write( response.getOutputStream(),false,responseData);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
