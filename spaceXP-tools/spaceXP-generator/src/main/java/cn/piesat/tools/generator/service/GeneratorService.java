package cn.piesat.tools.generator.service;

import cn.piesat.tools.generator.model.dto.TableDTO;

/**
 * <p/>
 * {@code @description}  :生成代码接口类
 * <p/>
 * <b>@create:</b> 2023/12/4 13:49.
 *
 * @author zhouxp
 */
public interface GeneratorService {
    byte[] generatorCode(TableDTO tableDTO);
}
