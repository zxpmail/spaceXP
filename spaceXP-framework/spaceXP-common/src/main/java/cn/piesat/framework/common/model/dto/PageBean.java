package cn.piesat.framework.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p/>
 * {@code @description}  : 分页输入参数
 * <p/>
 * <b>@create:</b> 2023/9/22 10:37.
 *
 * @author zhouxp
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageBean {
    /**
     * 当前页数 默认 0
     */
    private Integer page = 0;
    /**
     * 页大小 默认10
     */
    private Integer size = 10;

    public Integer getSize() {
        return size > 100 ? 100 : size;
    }
}