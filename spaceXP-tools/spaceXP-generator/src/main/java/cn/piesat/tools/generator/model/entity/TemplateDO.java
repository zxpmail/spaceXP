package cn.piesat.tools.generator.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p/>
 * {@code @description}  :模版类
 * <p/>
 * <b>@create:</b> 2024/1/22 11:25.
 *
 * @author zhouxp
 */
@Data
@TableName("gen_template")
public class TemplateDO {
    private Integer id;
    private String type;
    private String name;
    private String content;
    private String path;
    private Integer isOnly;
}
