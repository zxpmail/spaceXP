package cn.piesat.tools.generator.model.dto;


import cn.piesat.tools.generator.model.vo.ProjectVO;
import cn.piesat.tools.generator.model.vo.TableVO;
import lombok.Data;

import java.util.List;


/**
 * <p/>
 * {@code @description}  :批量表DTO类
 * <p/>
 * <b>@create:</b> 2024/1/4 10:10.
 *
 * @author zhouxp
 */
@Data
public class TablesDTO extends ProjectVO {
    private String tablePrefix;
    private List<TableVO> tables;
}
