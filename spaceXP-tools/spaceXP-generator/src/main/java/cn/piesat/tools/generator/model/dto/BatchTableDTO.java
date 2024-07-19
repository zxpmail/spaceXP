package cn.piesat.tools.generator.model.dto;

import lombok.Data;

import java.util.List;

/**
 * <p/>
 * {@code @description}: 批量生成表代码实体类
 * <p/>
 * {@code @create}: 2024-07-17 17:27
 * {@code @author}: zhouxp
 */
@Data
public class BatchTableDTO {
    /**
     * 表集合信息
     */
    private List<TableDTO> tables;
    /**
     * 项目信息
     */
    private ProjectDTO project;
    /**
     * 表前缀
     */
    private String tablePrefix;
    /**
     * 表单布局
     */
    private Integer formLayout;
}
