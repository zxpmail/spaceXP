package cn.piesat.tools.generator.model.query;

import cn.piesat.framework.common.annotation.validator.group.AddGroup;
import cn.piesat.framework.common.annotation.validator.group.UpdateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * <p/>
 * {@code @description}  :属性类型查询实体类
 * <p/>
 * <b>@create:</b> 2023/12/6 8:59.
 *
 * @author zhouxp
 */
@Data
public class ProjectQuery {
    /**
     * 项目名
     */
    private String name;
    /**
     * 版本
     */
    private String version;
    /**
     * 作者
     */
    private String author;
}
