package cn.piesat.tools.generator.service;

import cn.piesat.tools.generator.model.dto.BatchTableDTO;
import cn.piesat.tools.generator.model.dto.ProjectDTO;
import cn.piesat.tools.generator.model.dto.TableDTO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p/>
 * {@code @description}  :生成代码接口类
 * <p/>
 * <b>@create:</b> 2023/12/4 13:49.
 *
 * @author zhouxp
 */
public interface GeneratorService {

    void genTableCode(List<TableDTO> table, HttpServletResponse response);

    void genProjectCode(ProjectDTO projectDTO, HttpServletResponse response);

    void genBatchTableCode(BatchTableDTO batchTableDTO, HttpServletResponse response);
}
