package cn.piesat.tools.generator.service;

import cn.piesat.tools.generator.model.dto.TableDTO;
import cn.piesat.tools.generator.model.dto.TablesDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p/>
 * {@code @description}  :生成代码接口类
 * <p/>
 * <b>@create:</b> 2023/12/4 13:49.
 *
 * @author zhouxp
 */
public interface GeneratorService {
    void generatorCode(TableDTO tableDTO, HttpServletResponse response);

    void generatorCode(TablesDTO tablesDTO, HttpServletResponse response);
}
