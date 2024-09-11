package cn.piesat.test.flowable.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p/>
 * {@code @description}:审批列表查询结果
 * <p/>
 * {@code @create}: 2024-09-11 15:41
 * {@code @author}: zhouxp
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskVO {
    private String id;
    private String day;
    private String name;
}
