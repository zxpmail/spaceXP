package cn.piesat.tools.generator.model;

import cn.piesat.tools.generator.model.vo.ProjectVO;
import lombok.Data;

import java.util.List;

/**
 * <p/>
 * {@code @description}  : 代码生成信息
 * <p/>
 * <b>@create:</b> 2023/12/26 17:54.
 *
 * @author zhouxp
 */
@Data
public class GeneratorInfo {
    private ProjectVO  project;
    private List<TemplateInfo> templates;
}
