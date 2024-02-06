package cn.piesat.tools.generator.model.query;


import lombok.Data;




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
    private String artifactId;
    /**
     * 版本
     */
    private String version;
    /**
     * 作者
     */
    private String author;
}
