package cn.piesat.tools.generator.model.vo;

import cn.piesat.tools.generator.model.entity.TableFieldDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p/>
 * {@code @description}  :数据表实体类
 * <p/>
 * <b>@create:</b> 2023/12/4 9:21.
 *
 * @author zhouxp
 */
@Data
@TableName("gen_table")
public class TableVO {
    @TableId
    private Long id;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 实体类名称
     */
    private String className;
    /**
     * 功能名
     */
    private String tableComment;

    /**
     * 数据源ID
     */
    private Long datasourceId;

}
