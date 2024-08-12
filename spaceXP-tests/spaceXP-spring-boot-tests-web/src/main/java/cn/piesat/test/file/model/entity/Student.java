package cn.piesat.test.file.model.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p/>
 * {@code @description}:
 * <p/>
 * {@code @create}: 2024-08-09 11:19
 * {@code @author}: zhouxp
 */
@Data
public class Student {
    private Integer id;
    @NotNull(message="名称不能为空！")
    private String name;
}
