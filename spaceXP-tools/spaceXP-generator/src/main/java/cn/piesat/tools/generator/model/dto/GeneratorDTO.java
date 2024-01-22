package cn.piesat.tools.generator.model.dto;


import lombok.Data;

import java.util.List;

/**
 * <p/>
 * {@code @description}  : 生成代码DTO
 * <p/>
 * <b>@create:</b> 2024/1/22 15:45.
 *
 * @author zhouxp
 */
@Data
public class GeneratorDTO {
    private ProjectDTO project;
    private List<TableDTO> tables;
}
