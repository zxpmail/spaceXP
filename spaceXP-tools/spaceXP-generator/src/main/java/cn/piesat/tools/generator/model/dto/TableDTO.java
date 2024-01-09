package cn.piesat.tools.generator.model.dto;


import cn.piesat.tools.generator.model.vo.ProjectVO;
import cn.piesat.tools.generator.model.vo.TableVO;
import lombok.Data;


/**
 * <p/>
 * {@code @description}  :表DTO类
 * <p/>
 * <b>@create:</b> 2024/1/4 10:10.
 *
 * @author zhouxp
 */
@Data
public class TableDTO extends TableVO {
    private TableVO table;
    private ProjectVO project;
}
