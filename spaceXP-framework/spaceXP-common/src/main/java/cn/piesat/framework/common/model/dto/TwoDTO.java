package cn.piesat.framework.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p/>
 * {@code @description}  :两个字段实体类
 * <p/>
 * <b>@create:</b> 2022/11/23 14:50.
 *
 * @author zhouxp
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TwoDTO<T,E> implements Serializable {
    private static final long serialVersionUID = 1964122905758342846L;

    private T first;
    private E second;

}
