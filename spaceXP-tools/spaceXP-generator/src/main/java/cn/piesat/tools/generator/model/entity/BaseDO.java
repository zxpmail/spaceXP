package cn.piesat.tools.generator.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p/>
 * {@code @description}  :基本DO
 * <p/>
 * <b>@create:</b> 2023/12/11 17:08.
 *
 * @author zhouxp
 */
@Data
public class BaseDO {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 是否删除
     */
    @TableLogic(value = "0",delval = "1")
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
