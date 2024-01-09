package cn.piesat.tools.generator.model.vo;

import lombok.Data;

import java.util.List;

/**
 * <p/>
 * {@code @description}  :生成项目VO
 * <p/>
 * <b>@create:</b> 2023/12/29 17:47.
 *
 * @author zhouxp
 */
@Data
public class GeneratorVO {
    private ProjectVO project;
    private List<TableVO> tables;
}
