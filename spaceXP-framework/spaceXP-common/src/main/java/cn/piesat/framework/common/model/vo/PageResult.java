package cn.piesat.framework.common.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p/>
 * {@code @description}  : 查询数据表分页时返回实体类
 * <p/>
 * <b>@create:</b> 2023/9/22 17:51.
 *
 * @author zhouxp
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PageResult implements Serializable {
    /**
     * 返回总条数
     */
    private Long total;
    /**
     * 返回记录数
     */
    private Object list;
}